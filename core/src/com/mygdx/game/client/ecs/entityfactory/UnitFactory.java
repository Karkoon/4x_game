package com.mygdx.game.client.ecs.entityfactory;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.ecs.component.ModelInstanceComp;
import com.mygdx.game.client.ecs.component.Name;
import com.mygdx.game.client.ecs.component.Position;
import com.mygdx.game.client.model.Coordinates;
import com.mygdx.game.client.util.ModelInstanceUtil;
import com.mygdx.game.config.UnitConfig;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Log
public class UnitFactory extends EntityFactory<UnitConfig> {

  @Inject
  public UnitFactory(@NonNull Engine engine, @NonNull GameScreenAssets assets) {
    super(engine, assets);
  }

  @Override
  public @NonNull Entity createEntity(@NonNull UnitConfig config, @NonNull Coordinates coordinates) {
    var entity = engine.createEntity();
    var position = engine.createComponent(Position.class);
    var name = setUpNameComponent(config);

    entity.add(position);
    entity.add(name);
    entity.add(setUpModelInstanceComp(config));
    engine.addEntity(entity);
    return entity;
  }

  private Name setUpNameComponent(@NonNull UnitConfig config) {
    var name = engine.createComponent(Name.class);
    name.setName(config.getName());
    name.setPolishName(config.getPolishName());
    return name;
  }

  private ModelInstanceComp setUpModelInstanceComp(@NonNull UnitConfig config) {
    var modelInstanceComp = engine.createComponent(ModelInstanceComp.class);
    modelInstanceComp.setModelInstanceFromModel(assets.getModel(config.getModelPath()));
    var texture = assets.getTexture(config.getTextureName());
    ModelInstanceUtil.setTexture(modelInstanceComp.getModelInstance(), texture);
    return modelInstanceComp;
  }
}