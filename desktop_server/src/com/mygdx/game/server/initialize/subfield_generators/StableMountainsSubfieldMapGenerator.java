package com.mygdx.game.server.initialize.subfield_generators;

import com.badlogic.gdx.utils.IntArray;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.config.SubFieldConfig;
import com.mygdx.game.server.ecs.entityfactory.SubFieldFactory;

import javax.inject.Inject;
import java.util.List;

public class StableMountainsSubfieldMapGenerator extends SubfieldMapGenerator {

  private final List<Integer> subfieldList =
      List.of(
          211,
          211,
          210,
          211,
          211,
          209,
          211,
          211,
          212,
          211,
          209,
          211,
          211,
          211,
          211,
          210,
          211,
          211,
          211
      );

  private final SubFieldFactory subFieldFactory;
  private final GameConfigAssets assets;

  @Inject
  protected StableMountainsSubfieldMapGenerator(
      SubFieldFactory subFieldFactory,
      GameConfigAssets assets
  ) {
    super(11);
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
