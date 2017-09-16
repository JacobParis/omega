package com.halfheartstudios.omega.components;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Jacob on 2017-09-11.
 */

public class Name extends Component {
    private HashSet<String> intents;

    public Name() {
        super("Name");
        intents = new HashSet<>();
        intents.add("!Name");
    }

    @Override
    public HashSet<String> getIntents() {
        return this.intents;
    }
}

