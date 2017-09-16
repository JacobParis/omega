package com.halfheartstudios.omega;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.halfheartstudios.omega.components.Component;
import com.halfheartstudios.omega.components.NullComponent;

import java.util.ArrayList;

import io.github.luizgrp.sectionedrecyclerviewadapter.SectionParameters;
import io.github.luizgrp.sectionedrecyclerviewadapter.StatelessSection;

/**
 * Created by jacob on 2017-07-04.
 */

public class EntitySection extends StatelessSection {
    private ArrayList<Entity> entityArrayList;
    private String headerText;

    public EntitySection(ArrayList<Entity> array, String headerText) {
        super(new SectionParameters.Builder(R.layout.entity_row)
                .headerResourceId(R.layout.section_header)
                .build());

        this.headerText = headerText;
        entityArrayList = array;

    }

    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        public TextView entityListHeader;

        public HeaderViewHolder(View v) {
            super(v);
            entityListHeader = (TextView) v.findViewById(R.id.section_list_header);
        }
    }

    public static class EntityHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ImageView entityImage;
        private TextView entityName;
        private Entity entity;

        public EntityHolder(View v) {
            super(v);

            entityImage = (ImageView) v.findViewById(R.id.entity_list_thumbnail);
            entityName = (TextView) v.findViewById(R.id.entity_list_title);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Context context = itemView.getContext();
            Intent showEntityIntent = new Intent(context, EntityViewActivity.class);
            showEntityIntent.putExtra("identifier", entity.getId());
            context.startActivity(showEntityIntent);
        }
    }
    @Override
    public int getContentItemsTotal() {
        return entityArrayList.size();
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new EntityHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
        EntityHolder entityHolder = (EntityHolder) holder;

        Entity entity = entityArrayList.get(position);
        entityHolder.entity = entity;

        String name = "(null)";
        Component nameComponent = ComponentManager.getComponentWithIntent("Name", entity);
        if(!(nameComponent instanceof NullComponent)) {
            name = nameComponent.getValue();
        }

        entityHolder.entityName.setText(name);
    }

    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
        headerViewHolder.entityListHeader.setText(headerText);
    }
}
