package com.mygdx.game.server.ecs.system;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.mygdx.game.core.ecs.component.Position;
import com.mygdx.game.server.ecs.component.UnitMovement;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Log
@All({UnitMovement.class, Position.class})
public class UnitMovementSystem extends IteratingSystem {
  private ComponentMapper<UnitMovement> unitMovementMapper;
  private ComponentMapper<Position> positionMapper;

  @Inject
  public UnitMovementSystem() {
  }

  @Override
  protected void process(int unit) {
    log.info("processing movement");
    var unitMovement = unitMovementMapper.get(unit);
    var fromSlot = unitMovement.getFromSlot();
    var toSlot = unitMovement.getToSlot();
    fromSlot.getEntities().removeValue(unit);
    toSlot.getEntities().add(unit);
    unitMovementMapper.remove(unit);
  }
}
