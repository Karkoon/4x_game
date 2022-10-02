package com.mygdx.game.server.network.gameinstance.services;

import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.annotations.AspectDescriptor;
import com.mygdx.game.core.ecs.component.Stats;
import com.mygdx.game.server.di.GameInstanceScope;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
@GameInstanceScope
public class EndTurnUtils extends WorldService {

  @AspectDescriptor(all = {Stats.class})
  private EntitySubscription unitSubscriber;

  private ComponentMapper<Stats> statsMapper;

  private final World world;

  @Inject
  EndTurnUtils(
      World world
  ) {
    world.inject(this);
    this.world = world;
  }

  public void makeEndTurnSteps() {
    resetUnitMovement();
  }

  private void resetUnitMovement() {
    for (int i = 0; i < unitSubscriber.getEntities().size(); i++) {
      int entityId = unitSubscriber.getEntities().get(i);
      var stats = statsMapper.get(entityId);
      stats.setMoveRange(stats.getMaxMoveRange());
      setDirty(entityId, Stats.class, world);
      world.process();
    }
  }
}
