package com.mygdx.game.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.assets.assetloaders.ArrayLoader;
import lombok.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class GameScreenAssets {

  @NonNull
  private final AssetManager assetManager;

  @Inject
  public GameScreenAssets(
      @NonNull AssetManager assetManager
  ) {
    this.assetManager = assetManager;
    initCustomLoaders();
  }

  public void loadAssetsAsync() {
    loadModels();
    loadTextures();
    loadSkin();
  }

  @NonNull
  public Model getModel(@NonNull String filename) {
    return assetManager.get(GameScreenAssetPaths.MODEL_DIR + filename);
  }

  @NonNull
  public Texture getTexture(@NonNull String path) {
    return assetManager.get(GameScreenAssetPaths.TEXTURE_DIR + path);
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

  private void loadModels() {
    loadDirectory(GameScreenAssetPaths.MODEL_DIR, ".g3db", Model.class);
  }

  private void loadTextures() {
    loadDirectory(GameScreenAssetPaths.TEXTURE_DIR, ".png", Texture.class);
    loadDirectory(GameScreenAssetPaths.ICON_DIR, ".png", Texture.class);
    assetManager.load(GameScreenAssetPaths.DEMO_TEXTURE_PATH, Texture.class);
  }

  private void loadSkin() {
    assetManager.load(GameScreenAssetPaths.DIALOG_SKIN, Skin.class);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private void initCustomLoaders() {
    assetManager.setLoader(Array.class, new ArrayLoader(new InternalFileHandleResolver()));
  }
}
