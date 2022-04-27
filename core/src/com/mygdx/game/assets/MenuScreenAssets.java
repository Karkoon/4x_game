package com.mygdx.game.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import lombok.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class MenuScreenAssets {

  @NonNull
  private final AssetManager assetManager;

  @Inject
  public MenuScreenAssets(AssetManager assetManager) {
    this.assetManager = assetManager;
  }

  public boolean update() {
    return assetManager.update();
  }

  public void loadAssetsAsync() {
    loadDirectory(MenuScreenAssetPaths.SHADER_DIR, ".frag", ShaderProgram.class);
    loadSkin();
  }

  private void loadSkin() {
    assetManager.load(MenuScreenAssetPaths.SKIN, Skin.class);
  }

  @NonNull
  public Texture getTexture(@NonNull String path) {
    return assetManager.get(path);
  }

  @NonNull
  public ShaderProgram getShaderProgram(@NonNull String path) {
    return assetManager.get(path);
  }

  @NonNull
  public Skin getSkin(@NonNull String path) {
    return assetManager.get(path);
  }

  private <T> void loadDirectory(@NonNull String path,
                                 @NonNull String suffix,
                                 @NonNull Class<T> assetType) {
    var dir = Gdx.files.internal(path).list(suffix);
    for (var i = 0; i < dir.length; i++) {
      assetManager.load(dir[i].path(), assetType);
    }
  }
}
