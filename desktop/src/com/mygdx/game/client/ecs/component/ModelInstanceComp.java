package com.mygdx.game.client.ecs.component;

import com.artemis.PooledComponent;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class ModelInstanceComp extends PooledComponent {
  private @NonNull ModelInstance modelInstance;

  @Override
  protected void reset() {
    this.modelInstance = null;
  }
}
