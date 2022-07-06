package com.mygdx.game.server.ecs.entityfactory;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.assets.GameScreenAssets;
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
  public SubFieldFactory(@NonNull World world,
                         @NonNull GameScreenAssets assets,
                         @NonNull GameRoomSyncer gameRoomSyncer) {
    super(world, assets);
    this.coordinatesMapper = world.getMapper(Coordinates.class);
    this.entityConfigIdMapper = world.getMapper(EntityConfigId.class);
    this.syncer = gameRoomSyncer;
  }

  @Override
  public int createEntity(@NonNull SubFieldConfig config, @NonNull Coordinates coordinates, Client clientOwner) {
    var entity = world.create();

    var position = setUpCoordinates(coordinates, entity);
    var entityConfigId = setUpEntityConfig(config, entity);

    syncer.sendComponent(position, entity);
    syncer.sendComponent(entityConfigId, entity);
    return entity;
  }

  private EntityConfigId setUpEntityConfig(@NonNull SubFieldConfig config, int entityId) {
    var entityConfigId = config.getId();
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
