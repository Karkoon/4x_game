package com.mygdx.game.client.entityfactory;

import com.badlogic.ashley.core.Engine;
import com.mygdx.game.ModelInstanceUtil;
import com.mygdx.game.assets.Assets;
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
public class UnitFactory {

  private final Engine engine;
  private final Assets assets;

  @Inject
  public UnitFactory(@NonNull Engine engine, @NonNull Assets assets) {
    this.engine = engine;
    this.assets = assets;
  }

  @NonNull
  public void createUnit(@NonNull UnitConfig config) {
    var entity = engine.createEntity();
    entity.add(engine.createComponent(PositionComponent.class));
    entity.add(setUpModelInstanceComponent(config));
    engine.addEntity(entity);
    log.log(Level.INFO, "Added a unit.");
  }

  private ModelInstanceComponent setUpModelInstanceComponent(UnitConfig config) {
    var modelInstanceComponent = engine.createComponent(ModelInstanceComponent.class);
    modelInstanceComponent.setModelInstanceFromModel(assets.getModel(config.getModelPath()));
    var texture = assets.getTexture(config.getTextureName());
    ModelInstanceUtil.setTexture(modelInstanceComponent.getModelInstance(), texture);
    return modelInstanceComponent;
  }
}
