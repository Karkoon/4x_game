package com.mygdx.game.server.initialize.subfield_generators;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.IntArray;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.config.SubFieldConfig;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.server.ecs.entityfactory.ComponentFactory;
import com.mygdx.game.server.ecs.entityfactory.SubFieldFactory;
import lombok.NonNull;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class DesertSubfieldMapGenerator extends SubfieldMapGenerator {

  private final List<Integer> subfieldList =
          List.of(
                  202,
                  203,
                  202,
                  202,
                  202,
                  203,
                  203,
                  203,
                  202,
                  202,
                  203,
                  203,
                  202,
                  202,
                  202,
                  203,
                  203,
                  202,
                  203
          );

  private final ComponentFactory componentFactory;
  private final SubFieldFactory subFieldFactory;
  private final GameConfigAssets assets;

  @Inject
  protected DesertSubfieldMapGenerator(
          @NonNull ComponentFactory componentFactory,
          @NonNull SubFieldFactory subFieldFactory,
          @NonNull GameConfigAssets assets) {
    super(3);
    this.componentFactory = componentFactory;
    this.subFieldFactory = subFieldFactory;
    this.assets = assets;
  }

  @Override
  public IntArray generateSubfield(int parentId) {
    var subFields = new IntArray();
    for (int i = 0; i < coordinatesList.size(); i++) {
      var coordinates = coordinatesList.get(i);
      var subfield = subfieldList.get(i);
      var subfieldConfig = assets.getGameConfigs().get(SubFieldConfig.class, subfield);

      var entityId = componentFactory.createEntityId();
      componentFactory.createCoordinateComponent(coordinates, entityId);
      subFieldFactory.createEntity(entityId, subfieldConfig);
      componentFactory.createSubFieldComponent(parentId, entityId);

      subFields.add(entityId);
    }
    return subFields;
  }
}
