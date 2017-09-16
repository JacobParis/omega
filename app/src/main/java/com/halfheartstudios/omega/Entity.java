package com.halfheartstudios.omega;

import android.support.annotation.Nullable;

import com.halfheartstudios.omega.components.Component;

import java.util.HashSet;
import java.util.UUID;

/**
 * Created by Jacob on 2017-09-14.
 */

public class Entity extends HashSet<Component> {
    private String id;

    public Entity(@Nullable String id) {
        super();
        this.id = (id == null) ? UUID.randomUUID().toString() : id;
    }

    public Entity() {
        super();
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}
