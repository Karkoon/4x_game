package com.mygdx.game.client.ecs.entityfactory;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.ecs.component.Field;
import com.mygdx.game.client.ecs.component.ModelInstanceComp;
import com.mygdx.game.client.ecs.component.Name;
import com.mygdx.game.client.ecs.component.Position;
import com.mygdx.game.client.ecs.component.Score;
import com.mygdx.game.client.util.ModelInstanceUtil;
import com.mygdx.game.config.FieldConfig;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Log
public class FieldFactory extends EntityFactory<FieldConfig> {

  private final ComponentMapper<Name> nameMapper;
  private final ComponentMapper<ModelInstanceComp> modelMapper;
  private final ComponentMapper<Position> positionMapper;
  private final ComponentMapper<Score> scoreMapper;
  private final ComponentMapper<Field> fieldMapper;

  @Inject
  public FieldFactory(@NonNull World world,
                      @NonNull GameScreenAssets assets) {
    super(world, assets);
    this.nameMapper = world.getMapper(Name.class);
    this.modelMapper = world.getMapper(ModelInstanceComp.class);
    this.positionMapper = world.getMapper(Position.class);
    this.scoreMapper = world.getMapper(Score.class);
    this.fieldMapper = world.getMapper(Field.class);
  }

  @Override
  public void createEntity(@NonNull FieldConfig config, int entity) {
    setUpModelInstanceComp(config, entity);
    setUpName(config, entity);
    setUpScore(config, entity);
    setUpField(config, entity);
    positionMapper.create(entity);
  }

  private void setUpModelInstanceComp(@NonNull FieldConfig config, int entityId) {
    var modelInstanceComp = modelMapper.create(entityId);
    modelInstanceComp.setModelInstanceFromModel(assets.getModel(config.getModelPath()));
    var texture = assets.getTexture(config.getTextureName());
    ModelInstanceUtil.setTexture(modelInstanceComp.getModelInstance(), texture);
  }

  private void setUpName(@NonNull FieldConfig config, int entityId) {
    var name = nameMapper.create(entityId);
    name.setName(config.getName());
    name.setPolishName(config.getPolishName());
  }

  private void setUpScore(FieldConfig config, int entityId) {
    var score = scoreMapper.create(entityId);
    score.setScoreValue(config.getScoreValue());
  }

  private void setUpField(FieldConfig config, int entityId) {
    var field = fieldMapper.create(entityId);
  }
}
