package com.mygdx.game.client.util;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.FloatAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import lombok.NonNull;
import lombok.extern.java.Log;

@Log
public final class ModelInstanceUtil {

  private ModelInstanceUtil() {
  }

  public static void setTexture(
      @NonNull ModelInstance modelInstance,
      @NonNull Texture texture
  ) {
    modelInstance.materials.get(0).set(TextureAttribute.createDiffuse(texture));
    modelInstance.materials.get(0).set(new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
    modelInstance.materials.get(0).set(new FloatAttribute(FloatAttribute.AlphaTest, 0.8f));
  }

  public static void tintColor(@NonNull ModelInstance modelInstance,
                               @NonNull Color color) {
    log.info("tinting color");
    modelInstance.materials.get(0).set(ColorAttribute.createDiffuse(color));
  }
}
