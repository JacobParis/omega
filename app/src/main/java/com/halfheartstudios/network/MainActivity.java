package com.halfheartstudios.network;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "MainActivity";
  private RecyclerView mContactsListView;
  private LinearLayoutManager mLayoutManager;
  private RecyclerAdapter mAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    AccountManager accountManager = AccountManager.get(this); //this is Activity
    Account account = new Account("MyAccount","com.halfheartstudios.network.CONTACTS");
    boolean success = accountManager.addAccountExplicitly(account,"password",null);
    if(success){
      Log.d(TAG,"Account created");
    }else{
      Log.d(TAG,"Account creation failed. Look at previous logs to investigate");
    }

    mContactsListView = (RecyclerView) findViewById(R.id.contacts_list);
    mLayoutManager = new LinearLayoutManager(this);
    mContactsListView.setLayoutManager(mLayoutManager);

    SectionedRecyclerViewAdapter sectionAdapter = new SectionedRecyclerViewAdapter();

    final HashMap<String, Contact> contacts = Contact.getContactsFromSystem(this);

    // Put contacts into lettered categories
    Map<Character, ArrayList<Contact>> map = new TreeMap<>();
    ArrayList<Contact> list;
    for(Contact contact : contacts.values()) {
      if(map.containsKey(contact.name.charAt(0))) {
        map.get(contact.name.charAt(0)).add(contact);
      } else {
        list = new ArrayList<>();
        list.add(contact);
        map.put(contact.name.charAt(0), list);
      }
    }

    // Create a section for each letter
    for(Character initial : map.keySet()) {
      sectionAdapter.addSection(new ContactsSection( map.get(initial), initial.toString()));
    }

    mContactsListView.setAdapter(sectionAdapter);
  }
}
