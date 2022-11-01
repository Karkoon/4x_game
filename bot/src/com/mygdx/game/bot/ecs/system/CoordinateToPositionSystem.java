package com.mygdx.game.bot.ecs.system;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.ecs.component.Movable;
import com.mygdx.game.client_core.ecs.component.Position;
import com.mygdx.game.core.ecs.component.Building;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.Field;
import com.mygdx.game.core.ecs.component.SubField;
import com.mygdx.game.core.ecs.component.Technology;
import com.mygdx.game.core.ecs.component.UnderConstruction;
import com.mygdx.game.core.util.PositionUtil;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
@All({Position.class, Coordinates.class})
@GameInstanceScope
public class CoordinateToPositionSystem extends IteratingSystem {

  private ComponentMapper<Building> buildingMapper;
  private ComponentMapper<Coordinates> coordinatesMapper;
  private ComponentMapper<Field> fieldMapper;
  private ComponentMapper<Movable> movableMapper;
  private ComponentMapper<Position> positionMapper;
  private ComponentMapper<SubField> subFieldMapper;
  private ComponentMapper<Technology> technologyMapper;
  private ComponentMapper<UnderConstruction> underConstructionMapper;

  @Inject
  public CoordinateToPositionSystem() {
    super();
  }

  @Override
  protected void process(int entityId) {
    var position = positionMapper.get(entityId);
    var retainedY = position.getValue().y;
    var toSet = new Vector3(0, 0, 0);
    if (fieldMapper.has(entityId)
        || movableMapper.has(entityId)
        || subFieldMapper.has(entityId)
        || buildingMapper.has(entityId)
        || underConstructionMapper.has(entityId)) {
      toSet = PositionUtil.generateWorldPositionForCoords(coordinatesMapper.get(entityId));
    } else if (technologyMapper.has(entityId)) {
      toSet = PositionUtil.generateTechnologyPositionForCoords(coordinatesMapper.get(entityId));
    }

    position.getValue().set(toSet);
    position.getValue().y = retainedY;
  }
}
