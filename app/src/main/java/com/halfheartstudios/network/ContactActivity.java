package com.halfheartstudios.network;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactActivity extends AppCompatActivity {

  private TextView contactName;
  private ListView contactDetails;
  private ListAdapter detailAdapter;
  private ArrayList<Detail> details;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_contact_page);

    Intent intent = this.getIntent();
    Bundle bundle = intent.getBundleExtra("agent");
    contactName = (TextView) findViewById(R.id.contact_page_name);
    String type = bundle.getString("type");
    details = new ArrayList<>();

    if(type.equals("person")) {

      Person person = Person.fromBundle(bundle);
      setTitle(person.getIdentifier());

      contactName.setText(person.getIdentifier());

      for (String number : person.getNumbers()) {
        details.add(new Detail("Phone Number", number));
      }
    }

    contactDetails = (ListView) findViewById(R.id.contact_page_details);
    detailAdapter = new DetailAdapter(this, details);
    contactDetails.setAdapter(detailAdapter);

  }
}
