package com.halfheartstudios.network;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jacob on 2017-07-04.
 */

public class DetailAdapter extends BaseAdapter {
  private Context mContext;
  private LayoutInflater mInflater;
  private ArrayList<Detail> mDataSource;

  public DetailAdapter(Context context, ArrayList<Detail> items) {
    mContext = context;
    mDataSource = items;
    mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  @Override
  public int getCount() {
    return mDataSource.size();
  }

  //2
  @Override
  public Object getItem(int position) {
    return mDataSource.get(position);
  }

  //3
  @Override
  public long getItemId(int position) {
    return position;
  }

  //4
  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    // Get view for row item
    View rowView = mInflater.inflate(R.layout.detail_row, parent, false);
    Detail detail = (Detail) getItem(position);

    TextView headingText = (TextView) rowView.findViewById(R.id.detail_heading);
    headingText.setText(detail.mStringHeading);

    TextView bodyText = (TextView) rowView.findViewById(R.id.detail_body);
    bodyText.setText(detail.mStringBody);

    return rowView;
  }
}
