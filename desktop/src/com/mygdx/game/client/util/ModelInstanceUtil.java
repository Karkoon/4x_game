package com.mygdx.game.client.util;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.attributes.BlendingAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import lombok.NonNull;

// TODO: 04.06.2022 bind the modelinstancecomp to the local entity which will be created from calls to server
public final class ModelInstanceUtil {

  private ModelInstanceUtil() {
  }

  public static void setTexture(@NonNull ModelInstance modelInstance,
                                @NonNull Texture texture) {
    modelInstance.materials.get(0).set(TextureAttribute.createDiffuse(texture));
    modelInstance.materials.get(0).set( new BlendingAttribute(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA));
  }
}
