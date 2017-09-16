package com.halfheartstudios.omega.components;

import com.halfheartstudios.omega.ComponentManager;
import com.halfheartstudios.omega.Entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;

/**
 * Created by Jacob on 2017-08-31.
 */

public class Component {
    private String type;

    private String value;
    private String entityValue;

    private HashSet<String> intents;

    public Component(String type) {
        this.type = type;

        intents = new HashSet<>();
    }

    public String getType() {
        return this.type;
    }

    public String getValue() {
        return this.value;
    }

    public Component setValue(String value) {
        this.value = value;
        return this;
    }

    public String getEntityValue() {
        return this.entityValue;
    }

    public Component setEntityValue(String entityId) {
        this.entityValue = entityId;
        Entity entity = ComponentManager.getInstance().getEntity(entityId);
        Component nameComponent = ComponentManager.getComponentWithIntent("Name", entity);
        this.value = nameComponent.getValue();

        return this;
    }

    public Component unlinkEntity() {
        this.entityValue = null;
        return this;
    }

    public HashSet<String> getIntents() {
        return this.intents;
    }

    public boolean hasIntent(String testIntent) {
        HashSet<String> intents = this.getIntents();
        for(String intent : intents) {
            if(intent == null) continue;

            if(intent.equals(testIntent)) {
                return true;
            }
        }

        return false;
    }

    public boolean hasPriorityIntent(String testIntent) {
        HashSet<String> intents = this.getIntents();
        for(String intent : intents) {
            if(intent == null) continue;
            String priorityIntent = "!".concat(testIntent);

            if(intent.equals(priorityIntent)) {
                return true;
            }
        }

        return false;
    }
    public String toString() {
        return this.getType() + ":" + this.getValue() + "@" + this.getEntityValue();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Component)) return false;

        Component component = (Component) o;

        if (!getType().equals(component.getType())) return false;
        return getValue().equals(component.getValue());

    }

    @Override
    public int hashCode() {
        int result = getType().hashCode();
        result = 31 * result + getValue().hashCode();
        return result;
    }


}


