package com.halfheartstudios.omega;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;

import com.halfheartstudios.omega.components.Component;

public class ComponentAddActivity extends AppCompatActivity {

  private FloatingActionButton fab;
  private Spinner documentTypeSpinner;
  private EditText documentValue;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.component_add_activity);

    documentTypeSpinner = (Spinner) findViewById(R.id.component_add_type);
    documentValue = (EditText) findViewById(R.id.component_add_field);
    fab = (FloatingActionButton) findViewById(R.id.component_add_save);
    fab.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra("create", true);

        String type = documentTypeSpinner.getSelectedItem().toString();
        String value = documentValue.getText().toString();

        String entityId = getIntent().getStringExtra("entityId");
        Component component = new Component(type, entityId);
        component.setValue(value);
        resultIntent.putExtra("component", component.toString());
        setResult(EntityViewActivity.RESULT_OK, resultIntent);
        finish();
      }
    });
  }
}
