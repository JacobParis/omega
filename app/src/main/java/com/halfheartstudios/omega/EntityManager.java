package com.halfheartstudios.omega;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.provider.ContactsContract;

import com.halfheartstudios.omega.components.Component;
import com.halfheartstudios.omega.components.Name;
import com.halfheartstudios.omega.components.NullComponent;
import com.halfheartstudios.omega.components.PhoneNumber;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;

/** Singleton class that stores and manipulates our entities
 * Created by Jacob on 2017-08-21.
 */

public class EntityManager {
    private static EntityManager instance;
    private Map<String, Entity> entityMap;

    private Context context;

    protected EntityManager() {
        entityMap = new TreeMap<>();
    }

    public static EntityManager getInstance() {
        if(instance == null) {
            instance = new EntityManager();
        }
        return instance;
    }

    public Map<String, Entity> getEntities() {
        return entityMap;
    }

    public void saveEntity(Entity entity, Context context) {
        if(context != null) this.context = context;

        String id = entity.getId();
        entityMap.put(id, entity);

        saveSharedPrefs(context);
    }
    public Entity getEntity(String id) {
        Entity entity = entityMap.get(id);

        return entity;
    }

    public void deleteComponent(Component component, Context context) {
        if(context != null) this.context = context;

        String entityId = component.getEntityId();
        Entity entity = getEntity(entityId);

        entity.removeComponent(component);

        // Delete saved Component
        SharedPreferences pref = context.getSharedPreferences("omega", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        Map<String, String> savedPreferences = (Map<String, String>) pref.getAll();

        for(String savedKey : savedPreferences.keySet()) {
            if(!savedKey.startsWith("omega")) { continue; }

            String componentId = savedKey.replaceAll(".*@|(:|=).*", "");
            if(!componentId.equals(component.getId())) continue;

            editor.remove(savedKey);
        }

        editor.commit();

    }
    public void createEntitiesFromContacts(Context context) {
        if(context != null) this.context = context;
        try {
            final String SORT_ORDER = ContactsContract.Data.DISPLAY_NAME;
            Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, SORT_ORDER);
            while (phones.moveToNext()) {
                Entity entity = new Entity();
                entity.setIndex(entityMap.size());

                // Name
                String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                if(name == null) continue;
                Name nameComponent = new Name(entity.getId()).setValue(name);
                entity.addComponent(nameComponent);

                // Number
                Component numberComponent = new PhoneNumber(entity.getId());
                numberComponent.setValue(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));
                entity.addComponent(numberComponent);

                entityMap.put(entity.getId(), entity);

                // TODO trigger a merge entities method that scans for identical components across entities and merges them to a single entity
            }
            phones.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map getLetteredAgentMap() {
        final Map<String, Entity> entities = getEntities();

        // Put contacts into lettered categories
        Map<Character, ArrayList<Entity>> map = new TreeMap<>();
        ArrayList<Entity> list;
        for(Entity entity : entities.values()) {
            String name = "¬null";
            Component nameComponent = entity.getComponentWithIntent("Name");
            if(!(nameComponent instanceof NullComponent)) {
                name = nameComponent.getValue();
            }

            Character initial = name.charAt(0);

            if(map.containsKey(initial)) {
                map.get(initial).add(entity);
            } else {
                list = new ArrayList<>();
                list.add(entity);
                map.put(initial, list);
            }
        }

        return map;
    }

    public void saveSharedPrefs(Context context) {
        if(context != null) this.context = context;
        SharedPreferences pref = context.getSharedPreferences("omega", Context.MODE_PRIVATE);
        if(pref == null) return;
        SharedPreferences.Editor editor = pref.edit();

        final Map<String, Entity> entityMap = getEntities();

        for(Entity entity : entityMap.values()) {
            if(entity.getId() == null) continue;

            ArrayList<Component> components = entity.getAllComponents();
            for(Component component : components) {
                String componentString = component.toString();
                String entityId = component.getEntityId();

                editor.putString("omega" + componentString, entityId);
            }
            editor.commit();
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
            Entity entity;
            if(entityMap.containsKey(entityId)) {
                entity = entityMap.get(entityId);
            } else {
                entity = new Entity();
                entity.setId(entityId);
            }
            String componentString = savedKey.substring(prefix.length());
            String type = componentString.replaceAll("@.*", "");
            switch(type) {
                case "Name":
                    Name name = Name.fromString(entityId, componentString);
                    entity.addComponent(name);
                    break;
                case "PhoneNumber":
                    PhoneNumber phoneNumber = PhoneNumber.fromString(entityId, componentString);
                    entity.addComponent(phoneNumber);
                    break;
                default:
                    Component component = Component.fromString(entityId, componentString);
                    entity.addComponent(component);
                    break;
            }
            entityMap.put(entityId, entity);
        }

    }
}
