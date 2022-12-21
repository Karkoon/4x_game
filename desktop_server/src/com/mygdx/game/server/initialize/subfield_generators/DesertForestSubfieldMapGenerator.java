package com.mygdx.game.server.initialize.subfield_generators;

import com.badlogic.gdx.utils.IntArray;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.config.SubFieldConfig;
import com.mygdx.game.server.ecs.entityfactory.SubFieldFactory;

import javax.inject.Inject;
import java.util.List;

public class DesertForestSubfieldMapGenerator extends SubfieldMapGenerator {

  private final List<Integer> subfieldList =
      List.of(
          205,
          206,
          206,
          205,
          206,
          206,
          206,
          206,
          208,
          206,
          205,
          208,
          206,
          205,
          208,
          207,
          208,
          206,
          208
      );

  private final SubFieldFactory subFieldFactory;
  private final GameConfigAssets assets;

  @Inject
  protected DesertForestSubfieldMapGenerator(
      SubFieldFactory subFieldFactory,
      GameConfigAssets assets
  ) {
    super(6);
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

      var entityId = subFieldFactory.createEntity(subfieldConfig, coordinates, parentId);

      subFields.add(entityId);
    }
    return subFields;
  }
}
