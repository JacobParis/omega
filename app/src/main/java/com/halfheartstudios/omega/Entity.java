package com.halfheartstudios.omega;

import com.halfheartstudios.omega.components.Component;
import com.halfheartstudios.omega.components.NullComponent;

import java.util.ArrayList;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

/**
 * Created by Jacob on 2017-08-21.
 */

public class Entity {
    private String id;
    private int index;
    private ArrayList<Component> components;

    public Entity() {
        this.id = UUID.randomUUID().toString();
        components = new ArrayList<>();
    }

    public Entity setId(String id) {
        this.id = id;

        return this;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getId() {
        return this.id;
    }

    public ArrayList<Component> getAllComponents() {
        return this.components;
    }

    public Map<String, ArrayList<Component>> getComponentsMap() {
        ArrayList<Component> components = getAllComponents();
        Map<String, ArrayList<Component>> componentsMap = new TreeMap<>();
        ArrayList<Component> current;
        for(Component component : components) {
            String type = component.getType();
            if(componentsMap.containsKey(type)) {
                current = componentsMap.get(type);
            } else {
                current = new ArrayList<>();
            }

            current.add(component);
            componentsMap.put(type, current);
        }

        return componentsMap;
    }
    public ArrayList<Component> getComponentsByType(String type) {
        ArrayList<Component> components = getAllComponents();
        ArrayList<Component> componentsByType = new ArrayList<>();

        for(Component component : components) {
            if(!component.getType().equals(type)) continue;
            componentsByType.add(component);
        }

        return componentsByType;
    }

    public void addComponent(Component component) {
        if(components.contains(component)) return;

        components.add(component);
    }

    public void addComponent(String type, Component component) {
        if(component.getType() != type) return;

        addComponent(component);
    }

    public void removeComponent(Component component) {
        if(components.contains(component)) {
            components.remove(component);
        }
    }

    public ArrayList<Component> getComponentsWithIntent(String intent) {
        ArrayList<Component> componentsWithIntent = new ArrayList<>();
        ArrayList<Component> components = getAllComponents();
        for(Component component : components) {
            if(!component.hasIntent(intent)) { continue; }

            componentsWithIntent.add(component);
        }

        return componentsWithIntent;
    }

    public Component getComponentWithIntent(String intent) {
        ArrayList<Component> componentsWithIntent = new ArrayList<>();
        ArrayList<Component> components = getAllComponents();
        for(Component component : components) {
            if(component.hasPriorityIntent(intent)) { return component; }

            if(!component.hasIntent(intent)) { continue; }

            componentsWithIntent.add(component);
        }
        if(componentsWithIntent.size() > 0) {
            return componentsWithIntent.get(0);
        }

        else return new NullComponent();
    }

    public void mergeEntity(Entity newEntity) {
        ArrayList<Component> newComponents = newEntity.getAllComponents();
        for(Component component : newComponents) {
            addComponent(component);
        }

        // TODO remove duplicates
        // TODO check for references to either exisiting UUID and unify
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entity)) return false;

        Entity entity = (Entity) o;

        return getId().equals(entity.getId());

    }
}
