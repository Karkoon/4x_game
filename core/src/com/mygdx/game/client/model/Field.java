package com.mygdx.game.client.model;

import com.mygdx.config.FieldConfig;
import lombok.NonNull;

public class Field extends GameEntity<FieldConfig> {

    public Field(int xPosition, int yPosition, @NonNull FieldConfig fieldConfig) {
        super(xPosition, yPosition, fieldConfig);
    }

}
