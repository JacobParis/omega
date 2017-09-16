package com.halfheartstudios.omega.components;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Jacob on 2017-09-15.
 */

public class Email extends Component {
    private String value;
    private HashSet<String> intents;

    public Email() {
        super("Email");
        intents = new HashSet<>();
        intents.add("Name");
        intents.add("Contact");
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public Component setValue(String value) {
        if(validEh(value)) {
            this.value = value;
        } else {
            this.value = "INVALID: " + value;
        }

        return this;
    }

    private boolean validEh(String value) {
        String regex = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(value);

        return matcher.matches();
    }
    @Override
    public HashSet<String> getIntents() {
        return this.intents;
    }
}
