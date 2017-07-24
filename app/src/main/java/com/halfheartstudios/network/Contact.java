package com.halfheartstudios.network;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jacob on 2017-07-03.
 */

public class Contact {
  public String name;
  public ArrayList<String> numbers;

  public int index;

  public static HashMap<String, Contact> getContactsFromSystem(Context context) {
    final HashMap<String, Contact> contactList = new HashMap<>();

    try {
      final String SORT_ORDER = ContactsContract.Data.DISPLAY_NAME;
      Cursor phones = context.getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, SORT_ORDER);
      while (phones.moveToNext()) {
        Contact contact = new Contact();

        contact.index = contactList.size();
        contact.name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));

        // Check if name already exists
        if(contactList.containsKey(contact.name)) {
          contact = contactList.get(contact.name);
        }

        contact.numbers.add(phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)));

        contactList.put(contact.name, contact);
      }
      phones.close();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return contactList;
  }

  public Contact() {
    numbers = new ArrayList<>();
  }

  public Bundle toBundle() {
    Bundle b = new Bundle();
    b.putString("name", name);
    b.putStringArrayList("numbers", numbers);

    return b;
  }

  public static Contact fromBundle(Bundle b) {
    Contact contact = new Contact();
    contact.name = b.getString("name");
    contact.numbers = b.getStringArrayList("numbers");

    return contact;
  }
}
