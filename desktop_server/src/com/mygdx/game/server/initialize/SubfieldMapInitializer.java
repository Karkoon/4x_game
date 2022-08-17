package com.mygdx.game.server.initialize;

import com.badlogic.gdx.utils.IntArray;
import com.mygdx.game.config.FieldConfig;
import com.mygdx.game.server.initialize.generators.SubfieldMapGeneratorsContainer;

import javax.inject.Inject;
import com.mygdx.game.server.di.GameInstanceScope;

@GameInstanceScope
public class SubfieldMapInitializer {

  private final SubfieldMapGeneratorsContainer subfieldMapGeneratorsContainer;

  @Inject
  public SubfieldMapInitializer(
      SubfieldMapGeneratorsContainer subfieldMapGeneratorsContainer
  ) {
    this.subfieldMapGeneratorsContainer = subfieldMapGeneratorsContainer;
  }

  public IntArray initializeSubarea(int fieldId, FieldConfig config) {
    return subfieldMapGeneratorsContainer.get(config.getId()).generateSubfield(fieldId);
  }

}
