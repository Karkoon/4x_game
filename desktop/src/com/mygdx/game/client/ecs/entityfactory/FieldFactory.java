package com.mygdx.game.client.ecs.entityfactory;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.ecs.component.ModelInstanceComp;
import com.mygdx.game.client.util.ModelInstanceUtil;
import com.mygdx.game.config.FieldConfig;
import com.mygdx.game.core.ecs.component.Name;
import com.mygdx.game.core.ecs.component.Position;
import com.mygdx.game.core.ecs.component.Slot;
import com.mygdx.game.core.model.Coordinates;
import com.mygdx.game.core.util.PositionUtil;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Log
public class FieldFactory extends EntityFactory<FieldConfig> {

  private final ComponentMapper<Position> positionMapper;
  private final ComponentMapper<Name> nameMapper;
  private final ComponentMapper<Slot> slotMapper;
  private final ComponentMapper<ModelInstanceComp> modelMapper;

  @Inject
  public FieldFactory(@NonNull World world,
                      @NonNull GameScreenAssets assets) {
    super(world, assets);
    this.positionMapper = world.getMapper(Position.class);
    this.nameMapper = world.getMapper(Name.class);
    this.slotMapper = world.getMapper(Slot.class);
    this.modelMapper = world.getMapper(ModelInstanceComp.class);
  }

  @Override
  public int createEntity(@NonNull FieldConfig config, @NonNull Coordinates coordinates) {
    var entity = world.create();
    setUpPosition(coordinates, entity);
    setUpModelInstanceComp(config, entity);
    slotMapper.create(entity);
    setUpName(config, entity);
    return entity;
  }

  private void setUpName(@NonNull FieldConfig config, int entityId) {
    var name = nameMapper.create(entityId);
    name.setName(config.getName());
    name.setPolishName(config.getPolishName());
  }

  private void setUpPosition(@NonNull Coordinates coordinates, int entityId) {
    var position = positionMapper.create(entityId);
    var positionFromCoordinates = PositionUtil.generateWorldPositionForCoords(coordinates);
    position.setPosition(positionFromCoordinates);
  }

  private void setUpModelInstanceComp(@NonNull FieldConfig config, int entityId) {
    var modelInstanceComp = modelMapper.create(entityId);
    modelInstanceComp.setModelInstanceFromModel(assets.getModel(config.getModelPath()));
    var texture = assets.getTexture(config.getTextureName());
    ModelInstanceUtil.setTexture(modelInstanceComp.getModelInstance(), texture);
  }
}
