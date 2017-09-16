package com.halfheartstudios.omega;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.halfheartstudios.omega.components.Component;
import com.halfheartstudios.omega.components.NullComponent;
import com.halfheartstudios.omega.data.Codes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.TreeMap;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class EntityViewActivity extends AppCompatActivity {

    private TextView entityName;

    private RecyclerView documentsListView;
    private LinearLayoutManager documentsListLayoutManager;
    private SectionedRecyclerViewAdapter sectionAdapter;


    private FloatingActionButton fab;


    private Entity entity;
    Map<String, ArrayList<Component>> componentMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entity_view_activity);

        entityName = (TextView) findViewById(R.id.entity_page_name);
        fab = (FloatingActionButton) findViewById(R.id.entity_page_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Context context = fab.getContext();
                Intent showAddComponentIntent = new Intent(context, ComponentAddActivity.class);
                showAddComponentIntent.putExtra("entityId", entity.getId());
                startActivityForResult(showAddComponentIntent, Codes.REQUEST_ADD_COMPONENT);
            }
        });

        documentsListView = (RecyclerView) findViewById(R.id.entity_page_component_list);
        documentsListLayoutManager = new LinearLayoutManager(this);
        documentsListView.setLayoutManager(documentsListLayoutManager);

        sectionAdapter = new SectionedRecyclerViewAdapter();
        documentsListView.setAdapter(sectionAdapter);

        Intent intent = this.getIntent();
        String id = intent.getStringExtra("identifier");
        setEntity(id);
        updateComponents();
    }

    private void setEntity(String id) {
        entity = ComponentManager.getInstance().getEntity(id);
    }

    private void updateComponents() {
        // Reload entity
        setEntity(entity.getId());

        // Set name and title
        Component nameComponent = ComponentManager.getComponentWithIntent("Name", entity);

        if(!(nameComponent instanceof NullComponent)) {
            String name = nameComponent.getValue();
            setTitle(name);
            entityName.setText(name);
        }

        // Sort by type and display
        componentMap = (TreeMap) ComponentManager.mapComponentsByType(entity);

        for(String componentType : componentMap.keySet()) {
            sectionAdapter.removeSection(componentType);
            sectionAdapter.addSection(componentType, new ComponentsSection(componentMap.get(componentType), componentType, sectionAdapter));
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {
            case Codes.REQUEST_ADD_COMPONENT:
                if(data == null) break;
                if(!data.getBooleanExtra("create", false)) break;

                String componentString = data.getStringExtra("component");
                Component component = ComponentManager.fromString(componentString);
                ComponentManager.getInstance().addComponent(component, entity.getId());

                updateComponents();
        }


    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);
        ContextMenu.ContextMenuInfo menuInfo = item.getMenuInfo();

        // Get the selected component
        int id = item.getGroupId();
        ArrayList<Component> componentArray = new ArrayList<>();
        Collection<ArrayList<Component>> componentCollection = componentMap.values();
        for(ArrayList<Component> componentList : componentCollection) {
            componentArray.add(new NullComponent()); // Pad the array for the section header
            componentArray.addAll(componentList);
        }
        Component component = componentArray.get(id);
        String entityId;
        switch((String) item.getTitle()) {
            case "Unlink":
                entityId = ComponentManager.getInstance().getComponentMap().get(component);
                Component newComponent = ComponentManager.asType(component.getType(), component.getValue());
                ComponentManager.getInstance().swapComponent(component, newComponent, entityId);
                updateComponents();
                break;
            case "Follow":
                entityId = component.getEntityValue();
                setEntity(entityId);
                updateComponents();
                break;
            case "Delete":
                ComponentManager.getInstance().deleteComponent(component, getApplicationContext());
                updateComponents();
                break;
        }
        return true;
    }
}
