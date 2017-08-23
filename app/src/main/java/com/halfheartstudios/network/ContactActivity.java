package com.halfheartstudios.network;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ContactActivity extends AppCompatActivity {

  private TextView mContactName;
  private ListView mContactDetails;
  private ListAdapter mDetailAdapter;
  private ArrayList<Detail> mDetails;
  private Agent agent;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_contact_page);

    Intent intent = this.getIntent();
    Bundle bundle = intent.getBundleExtra("agent");

    agent = Agent.fromBundle(bundle);
    setTitle(agent.getIdentifier());

    mContactName = (TextView) findViewById(R.id.contact_page_name);
    mContactName.setText(agent.getIdentifier());

    mDetails = new ArrayList<>();
    /*
    // Add Numbers
    for(int i = 0; i < agent.numbers.size(); i++) {
      mDetails.add(new Detail("Phone Number", agent.numbers.get(i)));
    }
  */
    mContactDetails = (ListView) findViewById(R.id.contact_page_details);
    mDetailAdapter = new DetailAdapter(this, mDetails);
    mContactDetails.setAdapter(mDetailAdapter);

  }
}
