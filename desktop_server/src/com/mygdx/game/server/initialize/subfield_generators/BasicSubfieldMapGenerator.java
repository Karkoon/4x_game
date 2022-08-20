package com.mygdx.game.server.initialize.subfield_generators;

import com.badlogic.gdx.utils.IntArray;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.config.SubFieldConfig;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.server.ecs.entityfactory.ComponentFactory;
import com.mygdx.game.server.ecs.entityfactory.SubFieldFactory;
import lombok.NonNull;

import javax.inject.Inject;

public class BasicSubfieldMapGenerator extends SubfieldMapGenerator {

  private final ComponentFactory componentFactory;
  private final SubFieldFactory subFieldFactory;
  private final GameConfigAssets assets;

  @Inject
  protected BasicSubfieldMapGenerator(
      @NonNull ComponentFactory componentFactory,
      @NonNull SubFieldFactory subFieldFactory,
      @NonNull GameConfigAssets assets) {
    super(0);
    this.componentFactory = componentFactory;
    this.subFieldFactory = subFieldFactory;
    this.assets = assets;
  }

  @Override
  public IntArray generateSubfield(int parentId) {
    var subFields = new IntArray();
    for (Coordinates coordinates : coordinatesList) {
      var subfieldConfig = assets.getGameConfigs().getRandom(SubFieldConfig.class);

      var entityId = componentFactory.createEntityId();
      componentFactory.createCoordinateComponent(coordinates, entityId);
      subFieldFactory.createEntity(entityId, subfieldConfig);
      componentFactory.createSubFieldComponent(parentId, entityId);

      subFields.add(entityId);
    }
    return subFields;
  }
}
