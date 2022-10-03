package com.mygdx.game.client_core.ecs.entityfactory;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.client_core.ecs.component.Movable;
import com.mygdx.game.client_core.ecs.component.Position;
import com.mygdx.game.config.UnitConfig;
import com.mygdx.game.core.ecs.component.Name;
import com.mygdx.game.core.ecs.component.Stats;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Log
public class UnitFactory {

  private ComponentMapper<Name> nameMapper;
  private ComponentMapper<Position> positionMapper;
  private ComponentMapper<Movable> movableMapper;
  private ComponentMapper<Stats> statsMapper;

  @Inject
  public UnitFactory(
      @NonNull World world
  ) {
    world.inject(this);
  }

  public void createEntity(@NonNull UnitConfig config, int entityId) {
    setUpNameComponent(config, entityId);
    setUpPositionComponent(entityId);
    setupStatsComponent(config, entityId);
    movableMapper.set(entityId, true);
  }

  private void setUpNameComponent(@NonNull UnitConfig config, int entityId) {
    var name = nameMapper.create(entityId);
    name.setName(config.getName());
    name.setPolishName(config.getPolishName());
  }

  private void setUpPositionComponent(int entityId) {
    Position position = positionMapper.create(entityId);
    position.getValue().set(0, 10, 0);
  }

  private void setupStatsComponent(@NonNull UnitConfig config, int entityId) {
    var stats = statsMapper.create(entityId);
    stats.setHp(config.getMaxHp());
    stats.setMaxHp(config.getMaxHp());
    stats.setDefense(config.getDefense());
    stats.setSightRadius(config.getSightRadius());
    stats.setAttackPower(config.getAttackPower());
    stats.setMoveRange(config.getMoveRange());
    stats.setMaxMoveRange(config.getMoveRange());
  }
}
