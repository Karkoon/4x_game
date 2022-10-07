package com.mygdx.game.client.ecs.entityfactory;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.client_core.ecs.entityfactory.Setter;
import com.mygdx.game.config.Config;
import com.mygdx.game.config.TechnologyConfig;
import com.mygdx.game.core.ecs.component.Coordinates;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Log
@Singleton
public class DesktopCoordinateSetter implements Setter {

  private ComponentMapper<Coordinates> coordinatesMapper;

  @Inject
  public DesktopCoordinateSetter(
      World world
  ) {
    world.inject(this);
  }

  @Override
  public Result set(Config config, int entityId) {
    if (config instanceof TechnologyConfig technologyConfig) {
      setUpCoordinateComp(technologyConfig, entityId);
      return Result.HANDLED;
    } else {
      return Result.REJECTED;
    }
  }

  private void setUpCoordinateComp(@NonNull TechnologyConfig config, int entityId) {
    var coordinate = coordinatesMapper.create(entityId);
    coordinate.setCoordinates(config.getX(), config.getY());
  }

}
