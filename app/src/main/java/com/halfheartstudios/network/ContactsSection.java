package com.halfheartstudios.network;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

/**
 * Created by jacob on 2017-07-04.
 */

public class ContactsSection extends StatelessSection {
  private ArrayList<Contact> mArray;
  private String mHeaderText;

  public ContactsSection( ArrayList<Contact> array, String headerText) {
    super(new SectionParameters.Builder(R.layout.contact_row)
    .headerResourceId(R.layout.section_header)
    .build());

    mHeaderText = headerText;
    mArray = array;

  }

  public static class HeaderViewHolder extends RecyclerView.ViewHolder {
    public TextView mContactListHeader;

    public HeaderViewHolder(View v) {
      super(v);
      mContactListHeader = (TextView) v.findViewById(R.id.contact_list_header);
    }
  }

  public static class ContactsHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private ImageView mContactImage;
    private TextView mContactName;
    private Contact mContact;

    public ContactsHolder(View v) {
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
  @Override
  public int getContentItemsTotal() {
    return mArray.size();
  }

  @Override
  public RecyclerView.ViewHolder getItemViewHolder(View view) {
    return new ContactsHolder(view);
  }

  @Override
  public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
    ContactsHolder contactsHolder = (ContactsHolder) holder;
    Contact contact = mArray.get(position);
    contactsHolder.mContactName.setText(contact.name);
    contactsHolder.mContact = contact;

  }

  @Override
  public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
    return new HeaderViewHolder(view);
  }

  @Override
  public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
    HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
    headerViewHolder.mContactListHeader.setText(mHeaderText);
  }
}
