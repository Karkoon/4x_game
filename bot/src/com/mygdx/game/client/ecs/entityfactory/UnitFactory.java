package com.mygdx.game.client.ecs.entityfactory;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.ecs.component.Movable;
import com.mygdx.game.config.UnitConfig;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Log
public class UnitFactory extends EntityFactory<UnitConfig> {

  private final ComponentMapper<Movable> movableMapper;

  @Inject
  public UnitFactory(@NonNull World world,
                     @NonNull GameScreenAssets assets) {
    super(world, assets);
    this.movableMapper = world.getMapper(Movable.class);
  }

  @Override
  public void createEntity(@NonNull UnitConfig config, int entity) {
    movableMapper.set(entity, true);
  }

}
