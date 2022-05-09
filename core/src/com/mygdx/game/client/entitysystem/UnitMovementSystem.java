package com.mygdx.game.client.entitysystem;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.ashley.core.Family;
import com.badlogic.ashley.systems.IteratingSystem;
import com.mygdx.game.client.component.PositionComponent;
import com.mygdx.game.client.component.UnitMovementComp;
import com.mygdx.game.client.service.SyncListener;
import dagger.Provides;
import lombok.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UnitMovementSystem extends IteratingSystem {

//  private final MovementSyncer movementSyncer;

  ComponentMapper<UnitMovementComp> unitMovementCompMapper = ComponentMapper.getFor(UnitMovementComp.class);
  ComponentMapper<PositionComponent> positionComponentMapper = ComponentMapper.getFor(PositionComponent.class);

  @Inject
  public UnitMovementSystem() {
    super(Family.all(UnitMovementComp.class).get());
//    this.movementSyncer = movementSyncer;
  }

  @Override
  protected void processEntity(Entity entity, float deltaTime) {
    var unitMovementComp = unitMovementCompMapper.get(entity);
    var fromEntity = unitMovementComp.getFromEntity();
    var toEntity = unitMovementComp.getToEntity();
    if (fromEntity != toEntity) {
      PositionComponent unitPositionComponent = positionComponentMapper.get(entity);
      unitPositionComponent.setPosition(positionComponentMapper.get(toEntity).getPosition());
      positionComponentMapper.get(toEntity).getPosition();
    }
  }

  interface MovementSyncer {
    void addSyncListener(@NonNull SyncListener<Boolean> listener);

    void sync(UnitMovementComp unitMovementComp);
  }
}
