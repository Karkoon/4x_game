package com.mygdx.game.client.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.math.Vector3;
import lombok.Data;
import lombok.NonNull;

@Data
public class PositionComponent implements Component {

    private @NonNull Vector3 position = new Vector3();

}
