package com.mygdx.game.client.component;

import com.badlogic.ashley.core.Component;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.Vector3;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class ModelInstanceComponent implements Component {
  private @NonNull ModelInstance modelInstance;

  public void setModelInstanceFromModel(@NonNull Model model) {
    this.modelInstance = new ModelInstance(model, new Vector3());
  }
}
