package com.halfheartstudios.network;

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
  private Contact mContact;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_contact_page);

    mContact = Contact.fromBundle(this.getIntent().getBundleExtra("contact"));
    setTitle(mContact.name);

    mContactName = (TextView) findViewById(R.id.contact_page_name);
    mContactName.setText(mContact.name);

    mDetails = new ArrayList<>();

    // Add Numbers
    for(int i=0; i < mContact.numbers.size(); i++) {
      mDetails.add(new Detail("Phone Number", mContact.numbers.get(i)));
    }

    mContactDetails = (ListView) findViewById(R.id.contact_page_details);
    mDetailAdapter = new DetailAdapter(this, mDetails);
    mContactDetails.setAdapter(mDetailAdapter);

  }
}
