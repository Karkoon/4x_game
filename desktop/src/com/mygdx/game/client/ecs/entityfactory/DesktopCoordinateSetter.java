package com.mygdx.game.client.ecs.entityfactory;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.client.util.ModelInstanceUtil;
import com.mygdx.game.config.ModelConfig;
import com.mygdx.game.config.TechnologyConfig;
import com.mygdx.game.core.ecs.component.Coordinates;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Log
public class DesktopCoordinateSetter {

  private final ComponentMapper<Coordinates> coordinatesMapper;

  @Inject
  public DesktopCoordinateSetter(@NonNull World world) {
    this.coordinatesMapper = world.getMapper(Coordinates.class);
  }

  public void set(@NonNull TechnologyConfig config, int entity) {
    setUpCoordinateComp(config, entity);
  }

  private void setUpCoordinateComp(@NonNull TechnologyConfig config, int entityId) {
    var coordinate = coordinatesMapper.create(entityId);
    coordinate.setCoordinates(config.getX(), config.getY());
  }
}
