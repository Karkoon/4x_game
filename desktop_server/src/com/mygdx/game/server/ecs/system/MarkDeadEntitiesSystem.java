package com.mygdx.game.server.ecs.system;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.systems.IteratingSystem;
import com.mygdx.game.core.ecs.component.Stats;
import com.mygdx.game.server.ecs.component.ChangeSubscribers;
import com.mygdx.game.server.ecs.component.Dead;

import javax.inject.Inject;

@All({Stats.class, ChangeSubscribers.class})
public class MarkDeadEntitiesSystem extends IteratingSystem {

  private ComponentMapper<Stats> statsComponentMapper;
  private ComponentMapper<Dead> deadComponentMapper;

  @Inject
  public MarkDeadEntitiesSystem() {
    super();
  }

  @Override
  protected void process(int entityId) {
    var stats = statsComponentMapper.get(entityId);
    if (stats.getHp() <= 0) {
      deadComponentMapper.set(entityId, true);
    }
  }

}
