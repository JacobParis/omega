package com.halfheartstudios.omega;

import android.content.Context;
import android.content.res.ColorStateList;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.MenuItem;
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
        super(new SectionParameters.Builder(R.layout.component).headerResourceId(R.layout.section_header).build());

        this.componentArrayList = components;
        this.headerText = headerText;
        this.adapter = adapter;
    }

    public class ComponentItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
        private TextView documentBodyText;
        private TextView documentHeaderText;
        private Component component;

        public ComponentItemViewHolder(View v) {
            super(v);

            documentBodyText = (TextView) v.findViewById(R.id.component_body);
            documentHeaderText = (TextView) v.findViewById(R.id.component_head);
            v.setOnClickListener(this);
            v.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = adapter.getPositionInSection(this.getAdapterPosition());
            Component component = componentArrayList.get(position);

        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            int position = this.getAdapterPosition();

            int sectionPosition = adapter.getPositionInSection(this.getAdapterPosition());
            Component component = componentArrayList.get(sectionPosition);

            String entityId = component.getEntityValue();
            if(entityId == null) {
                menu.add(position, v.getId(), 0, "Link");
                menu.add(position, v.getId(), 0, "Rename");

            } else {
                menu.add(position, v.getId(), 0, "Unlink");
                menu.add(position, v.getId(), 0, "Follow");
            }

            menu.add(position, v.getId(), 0, "Delete");//groupId, itemId, order, title
        }


    }

    public void notifyItemRemovedFromSection(int position) {
        adapter.notifyItemRemovedFromSection(this, position);
    }

    public class ComponentHeaderViewHolder extends RecyclerView.ViewHolder {
        private TextView headerText;

        public ComponentHeaderViewHolder(View v) {
            super(v);

            headerText = (TextView) v.findViewById(R.id.section_list_header);
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

        itemViewHolder.documentHeaderText.setText(component.getType());
        itemViewHolder.documentBodyText.setText(component.getValue());
        itemViewHolder.component = component;

        if(component.getEntityValue() != null) {
            itemViewHolder.documentBodyText.setTextColor(0xff3399CC);
        }
    }

}
