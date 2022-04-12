package com.mygdx.game.client.model;

import com.mygdx.config.EntityConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public abstract class GameEntity<T extends EntityConfig> {

    @NonNull
    private int xPosition;
    @NonNull
    private int yPosition;
    @NonNull
    private T entityConfig;
}
