package com.mygdx.game.client.ecs.component;

import com.artemis.PooledComponent;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
// TODO: 04.06.2022 bind it to local entities made from server calls
public class ModelInstanceComp extends PooledComponent {
  private @NonNull ModelInstance modelInstance;

  public void setModelInstanceFromModel(@NonNull Model model) {
    this.modelInstance = new ModelInstance(model, new Vector3());
  }

  @Override
  protected void reset() {
    this.modelInstance = null;
  }
}
