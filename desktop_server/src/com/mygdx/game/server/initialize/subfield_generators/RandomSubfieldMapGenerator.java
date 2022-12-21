package com.mygdx.game.server.initialize.subfield_generators;

import com.badlogic.gdx.utils.IntArray;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.config.SubFieldConfig;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.ecs.entityfactory.SubFieldFactory;

import javax.inject.Inject;

@GameInstanceScope
public class RandomSubfieldMapGenerator extends SubfieldMapGenerator {

  private final SubFieldFactory subFieldFactory;
  private final GameConfigAssets assets;

  @Inject
  protected RandomSubfieldMapGenerator(
      SubFieldFactory subFieldFactory,
      GameConfigAssets assets
  ) {
    super(0);
    this.subFieldFactory = subFieldFactory;
    this.assets = assets;
  }

  @Override
  public IntArray generateSubfield(int parentId) {
    var subFields = new IntArray();
    for (Coordinates coordinates : coordinatesList) {
      var subfieldConfig = assets.getGameConfigs().getRandom(SubFieldConfig.class);
      var subfieldId = subFieldFactory.createEntity(subfieldConfig, coordinates, parentId);
      subFields.add(subfieldId);
    }
    return subFields;
  }
}
