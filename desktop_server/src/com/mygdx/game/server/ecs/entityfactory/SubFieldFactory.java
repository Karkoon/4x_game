package com.mygdx.game.server.ecs.entityfactory;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.config.SubFieldConfig;
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
public class SubFieldFactory extends EntityFactory<SubFieldConfig> {

  private final ComponentMapper<Coordinates> coordinatesMapper;
  private final ComponentMapper<EntityConfigId> entityConfigIdMapper;
  private final GameRoomSyncer syncer;

  @Inject
  public SubFieldFactory(
      @NonNull World world,
      @NonNull GameConfigAssets assets,
      @NonNull GameRoomSyncer gameRoomSyncer
  ) {
    super(world, assets);
    this.coordinatesMapper = world.getMapper(Coordinates.class);
    this.entityConfigIdMapper = world.getMapper(EntityConfigId.class);
    this.syncer = gameRoomSyncer;
  }

  private EntityConfigId setUpEntityConfig(@NonNull SubFieldConfig config, int entityId) {
    var entityConfigId = config.getId();
    var entityConfigIdComponent = entityConfigIdMapper.create(entityId);
    entityConfigIdComponent.setId(entityConfigId);
    return entityConfigIdComponent;
  }

  @Override
  public void createEntity(int entityId, @NonNull SubFieldConfig config, Client clientOwner) {
    var entityConfigId = setUpEntityConfig(config, entityId);
    syncer.sendComponent(entityConfigId, entityId);
  }

}
