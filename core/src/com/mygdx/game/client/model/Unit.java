package com.mygdx.game.client.model;

import com.mygdx.game.config.UnitConfig;
import lombok.NonNull;

public class Unit extends GameEntity<UnitConfig> {

    public Unit(int xPosition, int yPosition, @NonNull UnitConfig unitConfig) {
        super(xPosition, yPosition, unitConfig);
    }

}


