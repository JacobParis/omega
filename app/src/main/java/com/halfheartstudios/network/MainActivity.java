package com.halfheartstudios.network;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Map;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "MainActivity";
  private RecyclerView agentsListView;
  private LinearLayoutManager agentsListLayoutManager;
  private SectionedRecyclerViewAdapter sectionAdapter;

  private AgentManager agentManager;
  private Context context;
  //Permissions Requests
  private final int PERMISSION_READ_CONTACTS = 0;

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

    // Request permission from the user for accessing contacts

    if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS}, PERMISSION_READ_CONTACTS);

    }

    context = this;
    agentManager = new AgentManager();

    agentsListView = (RecyclerView) findViewById(R.id.contacts_list);
    agentsListLayoutManager = new LinearLayoutManager(this);
    agentsListView.setLayoutManager(agentsListLayoutManager);

    sectionAdapter = new SectionedRecyclerViewAdapter();

    agentManager.loadSharedPrefs(this);
    updateAgentListSections();

    agentsListView.setAdapter(sectionAdapter);
  }

  private void updateAgentListSections() {

    Map<Character, ArrayList<Agent>> map = agentManager.getLetteredAgentMap();

    // Create / Update the section for each letter
    for(Character initial : map.keySet()) {
      String tag = initial.toString();
      sectionAdapter.removeSection(tag);
      sectionAdapter.addSection(tag, new AgentsSection( map.get(initial), tag));
    }
  }

  public boolean onCreateOptionsMenu(Menu menu) {

    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.agents, menu);
    return true;
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
    switch(requestCode) {
      case PERMISSION_READ_CONTACTS: {
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
          Log.d("PERMISSIONS", "Granted Read Contacts");
        } else {
          Log.d("PERMISSIONS", "Denied Read Contacts");
        }
        return;
      }
    }
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch(item.getItemId()) {
      case R.id.action_import:

        agentManager.createAgentsFromContacts(this);
        agentManager.saveSharedPrefs(this);
        updateAgentListSections();
        sectionAdapter.notifyDataSetChanged();
        return true;

      default:
        return super.onOptionsItemSelected(item);
    }
  }
}
