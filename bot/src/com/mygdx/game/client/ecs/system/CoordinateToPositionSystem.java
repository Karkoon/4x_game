package com.mygdx.game.client.ecs.system;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.mygdx.game.client.ecs.component.Position;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.util.PositionUtil;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Log
@Singleton
@All({Position.class, Coordinates.class})
public class CoordinateToPositionSystem extends IteratingSystem {

  private ComponentMapper<Position> positionMapper;
  private ComponentMapper<Coordinates> coordinatesMapper;

  @Inject
  public CoordinateToPositionSystem() {
  }

  @Override
  protected void process(int entityId) {
    // todo maybe make it more based on events? does it go against the ecs philosophy?
    var position = positionMapper.get(entityId);
    var retainedY = position.getPosition().y;
    position.setPosition(
        PositionUtil.generateWorldPositionForCoords(coordinatesMapper.get(entityId))
    );
    position.getPosition().y = retainedY;
  }
}
