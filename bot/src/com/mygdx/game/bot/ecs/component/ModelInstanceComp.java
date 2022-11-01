package com.mygdx.game.bot.ecs.component;

import com.artemis.PooledComponent;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.utils.IntMap;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class ModelInstanceComp extends PooledComponent {

  private @NonNull IntMap<ModelInstance> modelInstances = new IntMap<>(ModelIndex.values().length);

  @Override
  protected void reset() {
    this.modelInstances = new IntMap<>(ModelIndex.values().length);
  }

  public void setMainModel(ModelInstance mainModel) {
    modelInstances.put(ModelIndex.MAIN.ordinal(), mainModel);
  }

  public void setHighlight(ModelInstance highlight) {
    modelInstances.put(ModelIndex.HIGHLIGHT.ordinal(), highlight);
  }

  /**
   * order of these enums has meaning
   */
  private enum ModelIndex {
    MAIN, HIGHLIGHT
  }
}
