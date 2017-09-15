package com.halfheartstudios.omega;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.halfheartstudios.omega.components.Component;
import com.halfheartstudios.omega.components.NullComponent;
import com.halfheartstudios.omega.data.Codes;

import java.util.ArrayList;
import java.util.Map;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;

public class EntityViewActivity extends AppCompatActivity {

    private TextView entityName;

    private RecyclerView documentsListView;
    private LinearLayoutManager documentsListLayoutManager;
    private SectionedRecyclerViewAdapter sectionAdapter;


    private FloatingActionButton fab;


    private Entity entity;
    private ArrayList<Component> components;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent_page);

        entityName = (TextView) findViewById(R.id.agent_page_name);
        fab = (FloatingActionButton) findViewById(R.id.agent_page_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Context context = fab.getContext();
                Intent showAddDocumentIntent = new Intent(context, ComponentAddActivity.class);
                showAddDocumentIntent.putExtra("entityId", entity.getId());
                startActivityForResult(showAddDocumentIntent, Codes.REQUEST_ADD_COMPONENT);
            }
        });

        documentsListView = (RecyclerView) findViewById(R.id.agent_page_documents);
        documentsListLayoutManager = new LinearLayoutManager(this);
        documentsListView.setLayoutManager(documentsListLayoutManager);

        sectionAdapter = new SectionedRecyclerViewAdapter();
        documentsListView.setAdapter(sectionAdapter);

        Intent intent = this.getIntent();
        String id = intent.getStringExtra("identifier");
        loadEntity(id);
    }

    private void loadEntity(String id) {
        entity = EntityManager.getInstance().getEntity(id);
        components = entity.getAllComponents();

        // Set name and title
        Component nameComponent = entity.getComponentWithIntent("Name");
        if(!(nameComponent instanceof NullComponent)) {
            String name = nameComponent.getValue();
            setTitle(name);
            entityName.setText(name);
        }
        updateComponents();
    }

    private void updateComponents() {
        if(components.size() == 0) return;

        Map<String, ArrayList<Component>> componentMap = entity.getComponentsMap();

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
                Component component = Component.fromString(entity, componentString);
                entity.addComponent(component);
                EntityManager.getInstance().saveEntity(entity, getApplicationContext());
                loadEntity(entity.getId());
        }


    }
}
