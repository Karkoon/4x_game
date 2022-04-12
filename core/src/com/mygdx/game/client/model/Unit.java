package com.mygdx.game.client.model;

import com.mygdx.config.UnitConfig;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

public class Unit extends GameEntity<UnitConfig> {

    public Unit(@NonNull int xPosition, @NonNull int yPosition, @NonNull UnitConfig unitConfig) {
        super(xPosition, yPosition, unitConfig);
    }

}


