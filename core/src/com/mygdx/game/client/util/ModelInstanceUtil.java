package com.mygdx.game.client.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import lombok.NonNull;

public final class ModelInstanceUtil {

  private ModelInstanceUtil() {
  }

  public static void setTexture(@NonNull ModelInstance modelInstance,
                                @NonNull Texture texture) {
    modelInstance.materials.get(0).set(TextureAttribute.createDiffuse(texture));
  }
}
