package com.mygdx.game.client.ecs.entityfactory;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.client.ecs.component.NavigationDirection;
import com.mygdx.game.client.screen.Navigator;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.ecs.entityfactory.Setter;
import com.mygdx.game.config.Config;
import com.mygdx.game.config.FieldConfig;

import javax.inject.Inject;

@GameInstanceScope
public class NavigationDirectionSetter implements Setter {

  private ComponentMapper<NavigationDirection> directionMapper;

  @Inject
  public NavigationDirectionSetter(
      World world
  ) {
    world.inject(this);
    world.getMapper(NavigationDirection.class);
  }

  public Result set(Config config, int entity) {
    if (config instanceof FieldConfig) {
      directionMapper.create(entity).direction = Navigator.Direction.FIELD_SCREEN;
      return Result.HANDLED;
    } else {
      return Result.REJECTED;
    }
  }
}
