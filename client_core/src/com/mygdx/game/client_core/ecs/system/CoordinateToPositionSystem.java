package com.mygdx.game.client_core.ecs.system;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.client_core.ecs.component.Movable;
import com.mygdx.game.client_core.ecs.component.Position;
import com.mygdx.game.core.ecs.component.Building;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.Field;
import com.mygdx.game.core.ecs.component.SubField;
import com.mygdx.game.core.ecs.component.Technology;
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
  private ComponentMapper<Building> buildingMapper;
  private ComponentMapper<Technology> technologyMapper;
  private ComponentMapper<Movable> movableMapper;

  @Inject
  public CoordinateToPositionSystem() {
    super();
  }

  @Override
  protected void process(int entityId) {
    var position = positionMapper.get(entityId);
    var retainedY = position.getValue().y;
    var toSet = new Vector3(0, 0, 0);
    if (fieldMapper.has(entityId) || movableMapper.has(entityId) || subFieldMapper.has(entityId) || buildingMapper.has(entityId)) {
      toSet = PositionUtil.generateWorldPositionForCoords(coordinatesMapper.get(entityId));
    } else if (technologyMapper.has(entityId)) {
      toSet = PositionUtil.generateTechnologyPositionForCoords(coordinatesMapper.get(entityId));
    }
    position.getValue().set(toSet);
    position.getValue().y = retainedY;
  }
}
