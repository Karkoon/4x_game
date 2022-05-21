package com.mygdx.game.client.ecs.system;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mygdx.game.client.ecs.component.Position;
import com.mygdx.game.client.ecs.component.Slot;
import com.mygdx.game.client.ecs.component.UnitMovement;
import com.mygdx.game.client.service.SyncListener;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.badlogic.ashley.core.ComponentMapper.getFor;

@Singleton
@Log
public class UnitMovementSystem extends IteratingSystem {

//  private final MovementSyncer movementSyncer;

  private final ComponentMapper<UnitMovement> unitMovementMapper = getFor(UnitMovement.class);
  private final ComponentMapper<Position> positionMapper = getFor(Position.class);
  private final ComponentMapper<Slot> slotMapper = getFor(Slot.class);

  @Inject
  public UnitMovementSystem() {
    super(Family.all(UnitMovement.class, Position.class).get());
//    this.movementSyncer = movementSyncer;
  }

  @Override
  protected void processEntity(Entity unit, float deltaTime) {
    log.info("processing movement");
    var unitMovement = unitMovementMapper.get(unit);
    var fromField = unitMovement.getFromEntity();
    var toField = unitMovement.getToEntity();
    slotMapper.get(fromField).getEntities().removeValue(unit, true);
    slotMapper.get(toField).getEntities().add(unit);
    unit.remove(UnitMovement.class);



    var unitPosition = positionMapper.get(unit);
    var toFieldPosition = positionMapper.get(toField);
    unitPosition.setPosition(toFieldPosition.getPosition());
  }

  interface MovementSyncer {
    void addSyncListener(@NonNull SyncListener<Boolean> listener);

    void sync(UnitMovement unitMovement);
  }
}
