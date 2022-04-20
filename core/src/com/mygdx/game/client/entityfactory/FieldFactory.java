package com.mygdx.game.client.entityfactory;

import com.badlogic.ashley.core.Engine;
import com.badlogic.gdx.math.Vector3;
import com.mygdx.game.assets.Assets;
import com.mygdx.game.client.component.ModelInstanceComponent;
import com.mygdx.game.client.component.PositionComponent;
import com.mygdx.game.client.initialize.DrawUtil;
import com.mygdx.game.client.model.Field;
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
  public FieldFactory(@NonNull Engine engine, @NonNull Assets assets) {
    super(engine, assets);
  }

  @Override
  public @NonNull Field createEntity(@NonNull FieldConfig config, int x, int y) {
    var entity = engine.createEntity();
    var positionComponent = engine.createComponent(PositionComponent.class);
    positionComponent.setPosition(DrawUtil.generatePositionForField(x, y));
    entity.add(positionComponent);
    var modelInstanceComponent = engine.createComponent(ModelInstanceComponent.class);
    modelInstanceComponent.setModelInstanceFromModel(assets.getModel(config));
    entity.add(modelInstanceComponent);
    engine.addEntity(entity);
    log.log(Level.INFO, "Added a field.");
    return new Field(x, y, config, entity);
  }

}
