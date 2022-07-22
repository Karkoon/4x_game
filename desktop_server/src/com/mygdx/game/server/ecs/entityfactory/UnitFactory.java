package com.mygdx.game.server.ecs.entityfactory;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.config.UnitConfig;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.EntityConfigId;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.network.GameRoomSyncer;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Log
public class UnitFactory extends EntityFactory<UnitConfig> {

  private final ComponentMapper<Coordinates> coordinatesMapper;
  private final ComponentMapper<EntityConfigId> entityConfigIdMapper;
  private final GameRoomSyncer syncer;

  @Inject
  public UnitFactory(
      @NonNull World world,
      @NonNull GameConfigAssets assets,
      @NonNull GameRoomSyncer syncer
  ) {
    super(world, assets);
    this.coordinatesMapper = world.getMapper(Coordinates.class);
    this.entityConfigIdMapper = world.getMapper(EntityConfigId.class);
    this.syncer = syncer;
  }

  @Override
  public int createEntity(@NonNull UnitConfig config, @NonNull Coordinates coordinates, Client client) {
    var entity = world.create();

    var position = setUpCoordinates(coordinates, entity);
    var entityConfigId = setUpEntityConfig(entity);

    syncer.sendComponent(position, entity);
    syncer.sendComponent(entityConfigId, entity);
    return entity;
  }

  private EntityConfigId setUpEntityConfig(int entityId) {
    var entityConfigId = assets.getGameConfigs().getAny(UnitConfig.class).getId();
    var entityConfigIdComponent = entityConfigIdMapper.create(entityId);
    entityConfigIdComponent.setId(entityConfigId);
    return entityConfigIdComponent;
  }

  private Coordinates setUpCoordinates(Coordinates coordinates, int entityId) {
    var result = coordinatesMapper.create(entityId);
    result.setCoordinates(coordinates);
    return result;
  }
}
