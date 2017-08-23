package com.halfheartstudios.network;

import android.os.Bundle;

import java.util.ArrayList;

/**
 * Created by Jacob on 2017-08-21.
 */

public class Person extends Agent {
    // Names
    public String firstName;

    public String surname;
    public String primaryName;

    // Numbers
    public String primaryNumber;
    public ArrayList<String> numbers;

    // Work
    public String primaryOrganisation;
    public ArrayList<String> organisations;

    // Profile
    public String gender;
    public String birthday;

    public Person() {
        super();

        this.numbers = new ArrayList<>();
        this.organisations = new ArrayList<>();
    }

    public Person setFirstName(String  firstName) {
        super.setIdentifier(firstName);
        this.firstName =  firstName;

        return this;
    }

    public Person setSurname(String  surname) {
        this.surname =  surname;

        return this;
    }

    public Person setPrimaryName(String  primaryName) {
        this.primaryName =  primaryName;

        return this;
    }

    public Person setPrimaryNumber(String  primaryNumber) {
        this.primaryNumber =  primaryNumber;

        return this;
    }

    public Person setPrimaryOrganisation(String  primaryOrganisation) {
        this.primaryOrganisation =  primaryOrganisation;

        return this;
    }

    public Person setGender(String  gender) {
        this.gender =  gender;

        return this;
    }

    public Person setBirthday(String  birthday) {
        this.birthday =  birthday;

        return this;
    }

    @Override
    public Bundle toBundle() {
        Bundle b = new Bundle();
        b.putString("firstName", firstName);
        b.putString("surname", surname);
        b.putString("primaryName", primaryName);
        b.putString("primaryNumber", primaryNumber);
        b.putStringArrayList("numbers", numbers);
        b.putString("primaryOrganisation", primaryOrganisation);
        b.putStringArrayList("organisations", organisations);
        b.putString("gender", gender);
        b.putString("birthday", birthday);

        return b;
    }

    public static Person fromBundle(Bundle b) {
        Person person = new Person();

        return person.setSurname(b.getString("surname"))
        .setPrimaryName(b.getString("primaryName"))
        .setPrimaryNumber(b.getString("primaryNumber"))
        .setPrimaryOrganisation(b.getString("primaryOrganisation"))
        .setGender(b.getString("gender"))
        .setBirthday(b.getString("birthday"));
    }
}
