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

    public Person addNumber(String number) {
        this.numbers.add(number);

        return this;
    }

    public ArrayList<String> getNumbers() {
        return this.numbers;
    }

    public Person setNumbers(ArrayList<String> numbers) {
        this.numbers = numbers;

        return this;
    }
    public Person setPrimaryOrganisation(String  primaryOrganisation) {
        this.primaryOrganisation =  primaryOrganisation;

        return this;
    }

    public Person setOrganisations(ArrayList<String> organisations) {
        this.organisations = organisations;

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

    public Person setIdentifier(String identifier) {
        super.setIdentifier(identifier);

        return this;
    }

    @Override
    public Bundle toBundle() {
        Bundle b = new Bundle();
        b.putString("type", "person");
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

        person.setFirstName(b.getString("firstName"));
        person.setSurname(b.getString("surname"));
        person.setPrimaryName(b.getString("primaryName"));
        person.setPrimaryNumber(b.getString("primaryNumber"));
        person.setNumbers(b.getStringArrayList("numbers"));
        person.setPrimaryOrganisation(b.getString("primaryOrganisation"));
        person.setOrganisations(b.getStringArrayList("organisations"));
        person.setGender(b.getString("gender"));
        person.setBirthday(b.getString("birthday"));

        return person;
    }
}
