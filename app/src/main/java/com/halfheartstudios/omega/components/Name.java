package com.halfheartstudios.omega.components;

import java.util.ArrayList;

/**
 * Created by Jacob on 2017-09-11.
 */

public class Name extends Component {
    private ArrayList<String> intents;
    public Name(String entityId) {
        super("Name", entityId);
        intents = new ArrayList<>();
        intents.add("!Name");
    }

    @Override
    public Name setId(String id) {
        super.setId(id);
        return this;
    }

    @Override
    public Name setValue(String value) {
        super.setValue(value);
        return this;
    }

    @Override
    public ArrayList<String> getIntents() {
        return this.intents;
    }

    @Override
    public String toString() {
        return "Name@" + this.getId() + ":" + this.getValue();
    }

    public static Name fromString(String entityId, String componentString) {
        if (!componentString.startsWith("Name@")) {
            throw new Error("Not a name string");
        }
        String id = componentString.replaceAll(".*@|:.*", "");
        String value = componentString.replaceAll(".*:", "");

        return new Name(entityId).setId(id).setValue(value);
    }
}

