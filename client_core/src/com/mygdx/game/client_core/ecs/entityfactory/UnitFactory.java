package com.mygdx.game.client_core.ecs.entityfactory;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client_core.ecs.component.Movable;
import com.mygdx.game.client_core.ecs.component.Name;
import com.mygdx.game.client_core.ecs.component.Position;
import com.mygdx.game.config.UnitConfig;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Log
public class UnitFactory extends EntityFactory<UnitConfig> {

  private final ComponentMapper<Name> nameMapper;
  private final ComponentMapper<Position> positionMapper;
  private final ComponentMapper<Movable> movableMapper;

  @Inject
  public UnitFactory(
      @NonNull World world,
      @NonNull GameScreenAssets assets
  ) {
    super(world, assets);
    this.nameMapper = world.getMapper(Name.class);
    this.positionMapper = world.getMapper(Position.class);
    this.movableMapper = world.getMapper(Movable.class);
  }

  @Override
  public void createEntity(@NonNull UnitConfig config, int entity) {
    setUpNameComponent(config, entity);
    positionMapper.create(entity).setPosition(new Vector3(0, 10, 0));
    movableMapper.set(entity, true);
  }

  private void setUpNameComponent(@NonNull UnitConfig config, int entityId) {
    var name = nameMapper.create(entityId);
    name.setName(config.getName());
    name.setPolishName(config.getPolishName());
  }
}
