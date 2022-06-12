package com.mygdx.game.server.ecs.entityfactory;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.config.UnitConfig;
import com.mygdx.game.core.ecs.component.Name;
import com.mygdx.game.core.ecs.component.Position;
import com.mygdx.game.core.model.Coordinates;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Log
public class UnitFactory extends EntityFactory<UnitConfig> {

  private final ComponentMapper<Position> positionMapper;
  private final ComponentMapper<Name> nameMapper;

  @Inject
  public UnitFactory(@NonNull World world,
                     @NonNull GameScreenAssets assets) {
    super(world, assets);
    this.positionMapper = world.getMapper(Position.class);
    this.nameMapper = world.getMapper(Name.class);
  }

  @Override
  public int createEntity(@NonNull UnitConfig config, @NonNull Coordinates coordinates) {
    var entity = world.create();
    positionMapper.create(entity);
    setUpNameComponent(config, entity);
    return entity;
  }

  private void setUpNameComponent(@NonNull UnitConfig config, int entityId) {
    var name = nameMapper.create(entityId);
    name.setName(config.getName());
    name.setPolishName(config.getPolishName());
  }
}
