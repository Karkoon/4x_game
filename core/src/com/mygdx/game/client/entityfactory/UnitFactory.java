package com.mygdx.game.client.entityfactory;

import com.badlogic.ashley.core.Engine;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.client.component.ModelInstanceComponent;
import com.mygdx.game.client.component.PositionComponent;
import com.mygdx.game.client.model.Unit;
import com.mygdx.game.client.component.StatsComponent;
import com.mygdx.game.config.UnitConfig;
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
  public UnitFactory(@NonNull Engine engine, @NonNull Assets assets) {
    super(engine, assets);
  }

  @Override
  public @NonNull Unit createEntity(@NonNull UnitConfig config, int x, int y) {
    var entity = engine.createEntity();
    var positionComponent = engine.createComponent(PositionComponent.class);
    entity.add(positionComponent);
    var modelInstanceComponent = engine.createComponent(ModelInstanceComponent.class);
    modelInstanceComponent.setModelInstanceFromModel(assets.getModel(config));
    entity.add(modelInstanceComponent);
    engine.addEntity(entity);
    log.log(Level.INFO, "Added a unit.");
    return new Unit(x, y, config, entity);
  }

  private ModelInstanceComponent setUpModelInstanceComponent(UnitConfig config) {
    var modelInstanceComponent = engine.createComponent(ModelInstanceComponent.class);
    modelInstanceComponent.setModelInstanceFromModel(assets.getModel(config.getModelPath()));
    var texture = assets.getTexture(config.getTextureName());
    ModelInstanceUtil.setTexture(modelInstanceComponent.getModelInstance(), texture);
    return modelInstanceComponent;
  }
}
