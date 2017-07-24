package com.halfheartstudios.network;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by jacob on 2017-07-03.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ContactHolder> {
  private HashMap<String, Contact> mArray;
  private ArrayList<String> contactKeys;

  public static class ContactHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private ImageView mContactImage;
    private TextView mContactName;
    private Contact mContact;

    public ContactHolder(View v) {
      super(v);

      mContactImage = (ImageView) v.findViewById(R.id.contact_list_thumbnail);
      mContactName = (TextView) v.findViewById(R.id.contact_list_title);
      v.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      Context context = itemView.getContext();
      Intent showContactIntent = new Intent(context, ContactActivity.class);
      showContactIntent.putExtra("contact", mContact.toBundle());
      context.startActivity(showContactIntent);
    }
  }
  public RecyclerAdapter(HashMap<String, Contact> array) {
    mArray = array;
    contactKeys = new ArrayList<>(array.keySet());
  }

  @Override
  public ContactHolder onCreateViewHolder(ViewGroup parent, int viewType) {
    View inflatedView = LayoutInflater.from(parent.getContext())
        .inflate(R.layout.contact_row, parent, false);

    return new ContactHolder(inflatedView);
  }

  @Override
  public void onBindViewHolder(ContactHolder holder, int position) {
    Contact contact = mArray.get(contactKeys.get(position));
    holder.mContactName.setText(contact.name);
    holder.mContact = contact;
  }

  @Override
  public int getItemCount() {
    return mArray.size();
  }
}
