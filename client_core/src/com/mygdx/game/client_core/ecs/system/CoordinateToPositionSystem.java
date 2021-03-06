package com.mygdx.game.client_core.ecs.system;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.client_core.ecs.component.Movable;
import com.mygdx.game.client_core.ecs.component.Position;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.Field;
import com.mygdx.game.core.ecs.component.SubField;
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
  private ComponentMapper<Field> fieldMapper;
  private ComponentMapper<SubField> subFieldMapper;
  private ComponentMapper<Movable> movableMapper;

  @Inject
  public CoordinateToPositionSystem() {
    super();
  }

  @Override
  protected void process(int entityId) {
    // todo maybe make it more based on events? does it go against the ecs philosophy?
    var position = positionMapper.get(entityId);
    var retainedY = position.getPosition().y;
    var toSet = new Vector3(0, 0, 0);
    if (fieldMapper.has(entityId) || movableMapper.has(entityId)) {
      toSet = PositionUtil.generateWorldPositionForCoords(coordinatesMapper.get(entityId));
    } else if (subFieldMapper.has(entityId)) {
      toSet = PositionUtil.generateSubWorldPositionForCoords(coordinatesMapper.get(entityId));
    }
    position.setPosition(toSet);
    position.getPosition().y = retainedY;
  }
}
