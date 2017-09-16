package com.halfheartstudios.omega;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.halfheartstudios.omega.components.Component;
import com.halfheartstudios.omega.data.Codes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private RecyclerView agentsListView;
    private LinearLayoutManager agentsListLayoutManager;
    private SectionedRecyclerViewAdapter sectionAdapter;

    private FloatingActionButton fab;

    private Entity newEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entity_list_activity);



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
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS}, Codes.PERMISSION_READ_CONTACTS);

        }


        agentsListView = (RecyclerView) findViewById(R.id.entity_list);
        agentsListLayoutManager = new LinearLayoutManager(this);
        agentsListView.setLayoutManager(agentsListLayoutManager);

        sectionAdapter = new SectionedRecyclerViewAdapter();
        agentsListView.setAdapter(sectionAdapter);

        fab = (FloatingActionButton) findViewById(R.id.entity_list_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                newEntity = new Entity();
                Context context = fab.getContext();
                Intent showAddEntityIntent = new Intent(context, ComponentAddActivity.class);
                showAddEntityIntent.putExtra("entityId", newEntity.getId());
                startActivityForResult(showAddEntityIntent, Codes.REQUEST_ADD_ENTITY);
            }
        });

        refresh();
    }

    private void updateEntityListSections() {

        Map<String, Entity> entityMap = new HashMap<>();
        Map<Component, String> componentMap = ComponentManager.getInstance().getComponentMap();

        // Invert the componentMap
        for(Component component : componentMap.keySet()) {
            String entityId = componentMap.get(component);
            Entity entity;
            if(entityMap.containsKey(entityId)) {
                entity = entityMap.get(entityId);
            } else {
                entity = new Entity(entityId);
            }
            entity.add(component);
            entityMap.put(entityId, entity);
        }

        Map<Character, ArrayList<Entity>> initialMap = new TreeMap<>();

        // Find the entity's name
        for(Entity entity : entityMap.values()) {
            Component nameComponent = ComponentManager.getComponentWithIntent("Name", entity);
            if(nameComponent == null) continue;
            String name = nameComponent.getValue();
            if(name == null) continue;
            Character initial = name.charAt(0);

            ArrayList<Entity> entityList;
            if(initialMap.containsKey(initial)) {
                entityList = initialMap.get(initial);
            } else {
                entityList = new ArrayList<>();
            }

            entityList.add(entity);
            initialMap.put(initial, entityList);
        }

        // Create / Update the section for each letter
        for(Character initial : initialMap.keySet()) {
            String tag = initial.toString();
            sectionAdapter.removeSection(tag);
            sectionAdapter.addSection(tag, new EntitySection( initialMap.get(initial), tag));
        }
    }

    private void refresh() {
        ComponentManager.getInstance().loadSharedPrefs(this);
        updateEntityListSections();
        sectionAdapter.notifyDataSetChanged();
    }
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.entity_list_menu, menu);
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch(requestCode) {
            case Codes.PERMISSION_READ_CONTACTS: {
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
                ComponentManager.getInstance().createEntitiesFromContacts(this);
                refresh();
                return true;

            case R.id.action_refresh:
                refresh();
                return true;

            case R.id.action_merge:
                refresh();
                return true;

            case R.id.action_clear:
                getApplicationContext().getSharedPreferences("omega", 0).edit().clear().commit();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case Codes.REQUEST_ADD_ENTITY:
                if(data == null) break;
                if(!data.getBooleanExtra("create", false)) break;

                String componentString = data.getStringExtra("component");
                Component component = ComponentManager.fromString(componentString);
                ComponentManager.getInstance().addComponent(component, newEntity.getId());
                refresh();
                break;
        }


    }
    @Override
    public void onRestart() {
        super.onRestart();
        refresh();
    }
}
