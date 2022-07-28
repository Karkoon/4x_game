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
  public void createEntity(int entityId, @NonNull UnitConfig config, Client client) {
    var entityConfigId = setUpEntityConfig(entityId);

    syncer.sendComponent(entityConfigId, entityId);
  }

  private EntityConfigId setUpEntityConfig(int entityId) {
    var entityConfigId = assets.getGameConfigs().getAny(UnitConfig.class).getId();
    var entityConfigIdComponent = entityConfigIdMapper.create(entityId);
    entityConfigIdComponent.setId(entityConfigId);
    return entityConfigIdComponent;
  }

}
