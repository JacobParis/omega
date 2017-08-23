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

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
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
        b.putString("identifier", identifier);

        return b;
    }

    public static Agent fromBundle(Bundle b) {
        Agent agent = new Agent();

        agent.setIdentifier(b.getString("identifier"));

        return agent;
    }
}
