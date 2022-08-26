package com.mygdx.game.client.ecs.entityfactory;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.client.ecs.component.Choosable;
import com.mygdx.game.client_core.ecs.entityfactory.Setter;
import com.mygdx.game.config.Config;
import com.mygdx.game.config.FieldConfig;
import com.mygdx.game.config.SubFieldConfig;
import com.mygdx.game.config.UnitConfig;
import lombok.NonNull;

import javax.inject.Inject;

public class ChooseableSetter implements Setter {

  private final ComponentMapper<Choosable> clickableMapper;

  @Inject
  public ChooseableSetter(
      @NonNull World world
  ) {
    this.clickableMapper = world.getMapper(Choosable.class);
  }

  @Override
  public Result set(Config config, int entityId) {
    if (config instanceof FieldConfig || config instanceof UnitConfig || config instanceof SubFieldConfig) {
      clickableMapper.set(entityId, true);
      return Result.HANDLED;
    } else {
      return Result.REJECTED;
    }
  }
}
