package com.halfheartstudios.omega;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.halfheartstudios.omega.components.Component;
import com.halfheartstudios.omega.components.Email;
import com.halfheartstudios.omega.components.Name;
import com.halfheartstudios.omega.components.NullComponent;
import com.halfheartstudios.omega.components.Organisation;
import com.halfheartstudios.omega.components.PhoneNumber;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.UUID;

/** Singleton class that stores and manipulates our entities
 * Created by Jacob on 2017-08-21.
 */

public class ComponentManager {
    private static ComponentManager instance;
    private Map<Component, String> componentMap;
    private Context context;

    protected ComponentManager() {
        componentMap = new HashMap<>();

    }

    public static ComponentManager getInstance() {
        if(instance == null) {
            instance = new ComponentManager();
        }
        return instance;
    }

    public HashSet<String> getEntities() {
        // These are unique because hashset only supports unique keys
        HashSet<String> entitySet = new HashSet<>(componentMap.values());

        return entitySet;
    }

    public Entity getEntity(String entityId) {
        Entity entity = new Entity(entityId);

        for(Component component : componentMap.keySet()) {
            if(componentMap.get(component).equals(entityId)) {
                entity.add(component);
            }
        }

        return entity;
    }

    public void addEntity(Entity entity) {
        String entityId = entity.getId();

        for(Component component : entity) {
            addComponent(component, entityId);
        }
    }
    public Map<Component, String> getComponentMap() {
        return this.componentMap;
    }

    public void swapComponent(Component oldComponent, Component newComponent, String entityId) {
        deleteComponent(oldComponent, this.context);
        addComponent(newComponent, entityId);
    }
    public void addComponent(Component component, String entityId) {
        componentMap.put(component, entityId);

        // Delete saved Component
        SharedPreferences pref = context.getSharedPreferences("omega", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        String componentString = component.toString();

        editor.putString("omega" + componentString, entityId);

        editor.commit();
    }

    public void deleteComponent(Component component, Context context) {
        if(context != null) this.context = context;

        if(!componentMap.containsKey(component)) throw new Error("Component does not exist");

        componentMap.remove(component);

        // Delete saved Component
        SharedPreferences pref = context.getSharedPreferences("omega", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        Map<String, String> savedPreferences = (Map<String, String>) pref.getAll();

        for(String savedKey : savedPreferences.keySet()) {
            if(!savedKey.startsWith("omega")) { continue; }

            String componentString = savedKey.substring("omega".length());
            if(!componentString.equals(component.toString())) continue;

            editor.remove(savedKey);
        }

        editor.commit();

    }

    public void createEntitiesFromContacts(Context context) {
        if(context != null) this.context = context;
        try {
            Map<String, String> profileMap = new HashMap<>();

            final String SORT_ORDER = ContactsContract.Data.DISPLAY_NAME;
            Cursor contactCursor = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, SORT_ORDER);
            if(contactCursor.moveToFirst()) {
                do {
                    // Build a profile from the stored data
                    profileMap.put("Name", contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
                    profileMap.put("PhoneNumber", contactCursor.getString(contactCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));

                    // Run through the profile map and add components for each field
                    Entity entity = new Entity();
                    for(String type : profileMap.keySet()) {
                        String value = profileMap.get(type);

                        Component component = ComponentManager.asType(type, value);
                        if(component instanceof NullComponent) continue;

                        if(componentMap.containsKey(component)) {
                            // If the component already exists, we want to be adding everything to that entity
                            entity.setId(componentMap.get(component));
                        } else {
                            entity.add(component);
                        }
                    }

                    addEntity(entity);
                } while (contactCursor.moveToNext());
            }

            contactCursor.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void loadSharedPrefs(Context context) {
        if(context != null) this.context = context;
        SharedPreferences pref = context.getSharedPreferences("omega", Context.MODE_PRIVATE);

        Map<String, String> savedPreferences = (Map<String, String>) pref.getAll();

        String prefix = "omega";

        for(String savedKey : savedPreferences.keySet()) {
            if(!savedKey.startsWith(prefix)) { continue; }

            String entityId = savedPreferences.get(savedKey);
            String componentString = savedKey.substring(prefix.length());

            Component component = ComponentManager.fromString(componentString);
            addComponent(component, entityId);

        }

    }

    public static Component getComponentWithIntent(String intent, Set<Component> componentSet) {
        ArrayList<Component> componentsWithIntent = new ArrayList<>();
        for(Component component : componentSet) {
            if(component.hasPriorityIntent(intent)) { return component; }

            if(!component.hasIntent(intent)) { continue; }

            componentsWithIntent.add(component);
        }
        if(componentsWithIntent.size() > 0) {
            return componentsWithIntent.get(0);
        }

        else return new NullComponent();
    }

    public static Map<String, ArrayList<Component>> mapComponentsByType(Set<Component> componentSet) {
        Map<String, ArrayList<Component>> componentMap = new TreeMap<>();
        ArrayList<Component> componentsByType;

        for(Component component : componentSet) {
            String type = component.getType();
            componentsByType = (componentMap.containsKey(type)) ? componentMap.get(type) : new ArrayList<Component>();
            componentsByType.add(component);
            componentMap.put(type, componentsByType);
        }

        return componentMap;
    }

    public static Component fromString(String componentString) {
        String type = componentString.replaceAll(":.*", "");
        String value = componentString.replaceAll(".*:|@.*", "");
        String entityId = componentString.replaceAll(".*@", "");

        if(entityId != null && !entityId.equals("null")) {
            return ComponentManager.asType(type, new Entity(entityId));
        }

        return ComponentManager.asType(type, value);
    }

    public static Component asType(String type, String value) {
        if(type == null || value == null) {
            return new NullComponent();
        }

        switch(type) {
            case "Name": return new Name().setValue(value);
            case "PhoneNumber": return new PhoneNumber().setValue(value);
            case "Email": return new Email().setValue(value);
            case "Organisation": return new Organisation().setValue(value);
            default: return new Component(type).setValue(value);
        }
    }

    public static Component asType(String type, Entity entity) {
        if(type == null || entity == null) {
            return new NullComponent();
        }

        String entityId = entity.getId();

        switch(type) {
            case "Name": return new Name().setEntityValue(entityId);
            case "PhoneNumber": return new PhoneNumber().setEntityValue(entityId);
            case "Email": return new Email().setEntityValue(entityId);
            case "Organisation": return new Organisation().setEntityValue(entityId);
            default: return new Component(type).setEntityValue(entityId);
        }
    }
}
