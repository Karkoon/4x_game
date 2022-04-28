package com.mygdx.game.client.entityfactory;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.mygdx.game.ModelInstanceUtil;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.component.ModelInstanceComponent;
import com.mygdx.game.client.component.PositionComponent;
import com.mygdx.game.client.initialize.PositionUtil;
import com.mygdx.game.client.model.Coordinates;
import com.mygdx.game.config.FieldConfig;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.logging.Level;

@Singleton
@Log
public class FieldFactory extends EntityFactory<FieldConfig> {

  @Inject
  public FieldFactory(@NonNull Engine engine, @NonNull GameScreenAssets assets) {
    super(engine, assets);
  }

  @Override
  public @NonNull Entity createEntity(@NonNull FieldConfig config, @NonNull Coordinates coordinates) {
    var entity = engine.createEntity();
    var positionComponent = engine.createComponent(PositionComponent.class);
    positionComponent.setPosition(PositionUtil.generateWorldPositionForCoords(coordinates));
    entity.add(positionComponent);
    entity.add(setUpModelInstanceComponent(config));
    engine.addEntity(entity);
    log.log(Level.INFO, "Added a field.");
    return entity;
  }

  private ModelInstanceComponent setUpModelInstanceComponent(FieldConfig config) {
    var modelInstanceComponent = engine.createComponent(ModelInstanceComponent.class);
    modelInstanceComponent.setModelInstanceFromModel(assets.getModel(config.getModelPath()));
    var texture = assets.getTexture(config.getTextureName());
    ModelInstanceUtil.setTexture(modelInstanceComponent.getModelInstance(), texture);
    return modelInstanceComponent;
  }

}
