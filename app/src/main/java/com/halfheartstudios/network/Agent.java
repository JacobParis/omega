package com.halfheartstudios.network;

import android.os.Bundle;

/**
 * Created by Jacob on 2017-08-21.
 */

public class Agent {
    private String identifier;
    private int index;

    public Agent() {
    }

    public Agent setIdentifier(String identifier) {
        this.identifier = identifier;

        return this;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getIdentifier() {
        return this.identifier;
    }
    public Bundle toBundle() {
        Bundle b = new Bundle();
        b.putString("type", "agent");
        b.putString("identifier", identifier);

        return b;
    }

    public static Agent fromBundle(Bundle b) {
        Agent agent = new Agent();

        agent.setIdentifier(b.getString("identifier"));

        return agent;
    }

    @Override
    public int hashCode() {
        return identifier.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Agent)) return false;

        Agent agent = (Agent) o;

        return getIdentifier().equals(agent.getIdentifier());

    }
}
