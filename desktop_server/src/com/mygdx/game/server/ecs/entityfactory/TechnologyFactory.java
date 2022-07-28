package com.mygdx.game.server.ecs.entityfactory;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.config.SubFieldConfig;
import com.mygdx.game.config.TechnologyConfig;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.EntityConfigId;
import com.mygdx.game.core.ecs.component.Technology;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.network.GameRoomSyncer;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Log
public class TechnologyFactory extends EntityFactory<TechnologyConfig> {

  private final ComponentMapper<EntityConfigId> entityConfigIdMapper;
  private final ComponentMapper<Technology> technologyMapper;
  private final GameRoomSyncer syncer;

  @Inject
  public TechnologyFactory(
      @NonNull World world,
      @NonNull GameConfigAssets assets,
      @NonNull GameRoomSyncer syncer
  ) {
    super(world, assets);
    this.entityConfigIdMapper = world.getMapper(EntityConfigId.class);
    this.technologyMapper = world.getMapper(Technology.class);
    this.syncer = syncer;
  }

  @Override
  public void createEntity(int entityId, @NonNull TechnologyConfig config, Client client) {
    var entityConfigId = setUpEntityConfig(config, entityId);
    syncer.sendComponent(entityConfigId, entityId);

  }

  private EntityConfigId setUpEntityConfig(@NonNull TechnologyConfig config, int entityId) {
    int configId = config.getId();
    var entityConfigIdComponent = entityConfigIdMapper.create(entityId);
    entityConfigIdComponent.setId(configId);
    return entityConfigIdComponent;
  }

}
