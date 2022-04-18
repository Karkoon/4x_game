package com.mygdx.game.client.entityfactory;

import com.badlogic.ashley.core.Engine;
import com.mygdx.game.ModelInstanceUtil;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.component.ModelInstanceComponent;
import com.mygdx.game.client.component.PositionComponent;
import com.mygdx.game.client.component.StatsComponent;
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
  private final GameScreenAssets gameScreenAssets;

  @Inject
  public UnitFactory(@NonNull Engine engine, @NonNull GameScreenAssets gameScreenAssets) {
    this.engine = engine;
    this.gameScreenAssets = gameScreenAssets;
  }

  @NonNull
  public void createUnit(@NonNull UnitConfig config) {
    var entity = engine.createEntity();
    entity.add(engine.createComponent(PositionComponent.class));
    entity.add(setUpModelInstanceComponent(config));
    entity.add(engine.createComponent(StatsComponent.class));
    engine.addEntity(entity);
    log.log(Level.INFO, "Added a unit.");
  }

  private ModelInstanceComponent setUpModelInstanceComponent(UnitConfig config) {
    var modelInstanceComponent = engine.createComponent(ModelInstanceComponent.class);
    modelInstanceComponent.setModelInstanceFromModel(gameScreenAssets.getModel(config.getModelPath()));
    var texture = gameScreenAssets.getTexture(config.getTextureName());
    ModelInstanceUtil.setTexture(modelInstanceComponent.getModelInstance(), texture);
    return modelInstanceComponent;
  }
}
