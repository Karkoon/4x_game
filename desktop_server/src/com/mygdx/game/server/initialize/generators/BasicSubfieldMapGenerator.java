package com.mygdx.game.server.initialize.generators;

import com.badlogic.gdx.utils.IntArray;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.config.SubFieldConfig;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.ecs.entityfactory.ComponentFactory;
import com.mygdx.game.server.ecs.entityfactory.SubFieldFactory;
import lombok.NonNull;

import javax.inject.Inject;

@GameInstanceScope
public class BasicSubfieldMapGenerator extends SubfieldMapGenerator {

  private final SubFieldFactory subFieldFactory;
  private final GameConfigAssets assets;

  @Inject
  protected BasicSubfieldMapGenerator(
      @NonNull SubFieldFactory subFieldFactory,
      @NonNull GameConfigAssets assets
  ) {
    super(0);
    this.subFieldFactory = subFieldFactory;
    this.assets = assets;
  }

  @Override
  public IntArray generateSubfields(int parentId) {
    var subFields = new IntArray();
    for (var coordinates : coordinatesList) {
      var subfieldConfig = assets.getGameConfigs().getAny(SubFieldConfig.class);
      var entityId = subFieldFactory.createEntity(subfieldConfig, coordinates, parentId);
      subFields.add(entityId);
    }
    return subFields;
  }
}
