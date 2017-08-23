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

public class AgentsSection extends StatelessSection {
  private ArrayList<Agent> agentArrayList;
  private String headerText;

  public AgentsSection(ArrayList<Agent> array, String headerText) {
    super(new SectionParameters.Builder(R.layout.agent_row)
    .headerResourceId(R.layout.section_header)
    .build());

    this.headerText = headerText;
    agentArrayList = array;

  }

  public static class HeaderViewHolder extends RecyclerView.ViewHolder {
    public TextView agentListHeader;

    public HeaderViewHolder(View v) {
      super(v);
      agentListHeader = (TextView) v.findViewById(R.id.contact_list_header);
    }
  }

  public static class AgentHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
    private ImageView agentImage;
    private TextView agentName;
    private Agent agent;

    public AgentHolder(View v) {
      super(v);

      agentImage = (ImageView) v.findViewById(R.id.agent_list_thumbnail);
      agentName = (TextView) v.findViewById(R.id.agent_list_title);
      v.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
      Context context = itemView.getContext();
      Intent showContactIntent = new Intent(context, ContactActivity.class);
      showContactIntent.putExtra("agent", agent.toBundle());
      context.startActivity(showContactIntent);
    }
  }
  @Override
  public int getContentItemsTotal() {
    return agentArrayList.size();
  }

  @Override
  public RecyclerView.ViewHolder getItemViewHolder(View view) {
    return new AgentHolder(view);
  }

  @Override
  public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
    AgentHolder agentHolder = (AgentHolder) holder;
   Agent agent = agentArrayList.get(position);
    agentHolder.agentName.setText(agent.getIdentifier());
    agentHolder.agent = agent;

  }

  @Override
  public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
    return new HeaderViewHolder(view);
  }

  @Override
  public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
    HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
    headerViewHolder.agentListHeader.setText(headerText);
  }
}
