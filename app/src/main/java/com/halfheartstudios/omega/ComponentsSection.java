package com.halfheartstudios.omega;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.halfheartstudios.omega.components.Component;

import java.util.ArrayList;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.SectionedRecyclerViewAdapter;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

/**
 * Created by Jacob on 2017-08-31.
 */

public class ComponentsSection extends StatelessSection {
    private SectionedRecyclerViewAdapter adapter;
    private ArrayList<Component> componentArrayList;
    private String headerText;

    public ComponentsSection(ArrayList<Component> components, String headerText, SectionedRecyclerViewAdapter adapter) {
        super(new SectionParameters.Builder(R.layout.document).headerResourceId(R.layout.section_header).build());

        this.componentArrayList = components;
        this.headerText = headerText;
        this.adapter = adapter;
    }

    public class ComponentItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView documentBodyText;
        private TextView documentHeaderText;
        private Component component;

        public ComponentItemViewHolder(View v) {
            super(v);

            documentBodyText = (TextView) v.findViewById(R.id.document_body);
            documentHeaderText = (TextView) v.findViewById(R.id.document_head);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Context context = v.getContext();
            int position = adapter.getPositionInSection(this.getAdapterPosition());
            Component component = componentArrayList.get(position);
            EntityManager.getInstance().deleteComponent(component, context);
            componentArrayList.remove(position);
            notifyItemRemovedFromSection(position);
        }
    }

    public void notifyItemRemovedFromSection(int position) {
        adapter.notifyItemRemovedFromSection(this, position);
    }

    public class ComponentHeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView headerText;

        public ComponentHeaderViewHolder(View v) {
            super(v);

            headerText = (TextView) v.findViewById(R.id.contact_list_header);
        }
    }

    @Override
    public int getContentItemsTotal() {
        return componentArrayList.size();
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new ComponentHeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        ComponentHeaderViewHolder headerViewHolder = (ComponentHeaderViewHolder) holder;

        headerViewHolder.headerText.setText(headerText);
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new ComponentItemViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        ComponentItemViewHolder itemViewHolder = (ComponentItemViewHolder) holder;
        Component component = componentArrayList.get(position);

        itemViewHolder.documentHeaderText.setText(component.getHeaderText());
        itemViewHolder.documentBodyText.setText(component.getValue());
        itemViewHolder.component = component;
    }
}
