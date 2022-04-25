package com.mygdx.game.client.entityfactory;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.mygdx.game.ModelInstanceUtil;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.component.ModelInstanceComponent;
import com.mygdx.game.client.component.PositionComponent;
import com.mygdx.game.config.UnitConfig;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.logging.Level;

@Singleton
@Log
public class UnitFactory extends EntityFactory<UnitConfig> {

  @Inject
  public UnitFactory(@NonNull Engine engine, @NonNull GameScreenAssets assets) {
    super(engine, assets);
  }

  @Override
  public @NonNull Entity createEntity(@NonNull UnitConfig config, int x, int y) {
    var entity = engine.createEntity();
    var positionComponent = engine.createComponent(PositionComponent.class);
    entity.add(positionComponent);
    entity.add(setUpModelInstanceComponent(config));
    engine.addEntity(entity);
    log.log(Level.INFO, "Added a unit.");
    return entity;
  }

  private ModelInstanceComponent setUpModelInstanceComponent(UnitConfig config) {
    var modelInstanceComponent = engine.createComponent(ModelInstanceComponent.class);
    modelInstanceComponent.setModelInstanceFromModel(assets.getModel(config.getModelPath()));
    var texture = assets.getTexture(config.getTextureName());
    ModelInstanceUtil.setTexture(modelInstanceComponent.getModelInstance(), texture);
    return modelInstanceComponent;
  }
}
