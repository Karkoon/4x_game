package com.mygdx.game.client.model;

import com.mygdx.config.FieldConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Field extends GameEntity<FieldConfig> {

    @NonNull FieldConfig fieldConfig;

}
