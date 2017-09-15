package com.halfheartstudios.omega.components;

import com.halfheartstudios.omega.Entity;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by Jacob on 2017-08-31.
 */

public class Component {
    private String type;

    private String value;
    private String headerText;

    private String id;
    private String entityId;

    private ArrayList<String> intents;

    public Component(String type, String entityId) {
        this.type = type;
        this.entityId = entityId;
        this.id = UUID.randomUUID().toString();

        intents = new ArrayList<>();
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

    public String getHeaderText() {
        return this.headerText;
    }

    public Component setHeaderText(String headerText) {
        this.headerText = headerText;
        return this;
    }

    public String getEntityId() { return this.entityId; }

    public String getId() { return this.id; }

    public Component setId(String id) {
        this.id = id;
        return this;
    }

    public ArrayList<String> getIntents() {
        return this.intents;
    }
    public boolean hasIntent(String testIntent) {
        ArrayList<String> intents = this.getIntents();
        for(String intent : intents) {
            if(intent == null) continue;

            if(intent.equals(testIntent)) {
                return true;
            }
        }

        return false;
    }

    public boolean hasPriorityIntent(String testIntent) {
        ArrayList<String> intents = this.getIntents();
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
        return this.getType() + "@" + this.getId() + ":" + this.getValue();
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

    public static Component fromString(String entityId, String componentString) {
        String id = componentString.replaceAll(".*@|:.*", "");
        String type = componentString.replaceAll("@.*", "");
        String value = componentString.replaceAll(".*:", "");

        return new Component(type, entityId).setId(id).setValue(value);
    }


    public static Component fromString(Entity entity, String componentString) {
        String entityId = entity.getId();

        return fromString(entityId, componentString);
    }

}


