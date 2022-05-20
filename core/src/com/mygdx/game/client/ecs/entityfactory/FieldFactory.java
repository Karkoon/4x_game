package com.mygdx.game.client.ecs.entityfactory;

import com.badlogic.ashley.core.Engine;
import com.badlogic.ashley.core.Entity;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.ecs.component.ModelInstanceComp;
import com.mygdx.game.client.ecs.component.Name;
import com.mygdx.game.client.ecs.component.Position;
import com.mygdx.game.client.ecs.component.Slot;
import com.mygdx.game.client.initialize.PositionUtil;
import com.mygdx.game.client.model.Coordinates;
import com.mygdx.game.client.util.ModelInstanceUtil;
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

    var position = engine.createComponent(Position.class);
    position.setValue(PositionUtil.generateWorldPositionForCoords(coordinates));
    var slot = engine.createComponent(Slot.class);
    var name = setUpName(config);

    entity.add(position);
    entity.add(name);
    entity.add(slot);
    entity.add(setUpModelInstanceComp(config));
    engine.addEntity(entity);
    log.log(Level.INFO, "Added a field.");
    return entity;
  }

  private Name setUpName(FieldConfig config) {
    var name = engine.createComponent(Name.class);
    name.setName(config.getName());
    name.setPolishName(config.getPolishName());
    return name;
  }

  private ModelInstanceComp setUpModelInstanceComp(FieldConfig config) {
    var modelInstanceComp = engine.createComponent(ModelInstanceComp.class);
    modelInstanceComp.setModelInstanceFromModel(assets.getModel(config.getModelPath()));
    var texture = assets.getTexture(config.getTextureName());
    ModelInstanceUtil.setTexture(modelInstanceComp.getModelInstance(), texture);
    return modelInstanceComp;
  }
}
