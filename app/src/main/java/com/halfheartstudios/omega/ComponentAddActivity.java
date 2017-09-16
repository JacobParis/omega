package com.halfheartstudios.omega;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.halfheartstudios.omega.components.Component;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ComponentAddActivity extends AppCompatActivity {

    private Spinner typeSpinner;
    private EditText valueEditText;
    private Spinner entityValueSpinner;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.component_add_activity);

        typeSpinner = (Spinner) findViewById(R.id.component_add_type);
        valueEditText = (EditText) findViewById(R.id.component_add_field);

        // Load target entities into spinner
        entityValueSpinner = (Spinner) findViewById(R.id.component_set_entityvalue);
        final Map<String, Entity> entityValueMap = new TreeMap<>();

        HashSet<String> entities = ComponentManager.getInstance().getEntities();
        for(String entityId : entities) {
            Entity entity = ComponentManager.getInstance().getEntity(entityId);
            Component nameComponent = ComponentManager.getComponentWithIntent("Name", entity);
            entityValueMap.put(nameComponent.getValue(), entity);
        }

        ArrayList<String> entityArrayList = new ArrayList<>();
        entityArrayList.add("");
        entityArrayList.addAll(entityValueMap.keySet());

        String[] entityNames = entityArrayList.toArray(new String[entityArrayList.size()]);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, entityNames);
        entityValueSpinner.setAdapter(adapter);

        // Set up save button
        fab = (FloatingActionButton) findViewById(R.id.component_add_save);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("create", true);

                String type = typeSpinner.getSelectedItem().toString();
                String value = valueEditText.getText().toString();
                String entityName = entityValueSpinner.getSelectedItem().toString();

                Component component;
                if(entityName != null && entityName.length() > 0) {
                    Entity entity = entityValueMap.get(entityName);
                    component = ComponentManager.asType(type, entity);
                } else {
                    component = ComponentManager.asType(type, value);
                }

                resultIntent.putExtra("component", component.toString());
                setResult(EntityViewActivity.RESULT_OK, resultIntent);
                finish();
            }
        });
    }
}
