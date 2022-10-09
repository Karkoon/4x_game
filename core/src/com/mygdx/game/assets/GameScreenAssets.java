package com.mygdx.game.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.assets.assetloaders.ArrayLoader;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Log
@Singleton
public class GameScreenAssets {

  @NonNull
  private final AssetManager assetManager;

  @Inject
  public GameScreenAssets(
      AssetManager assetManager
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
    return assetManager.get(GameScreenAssetPaths.MODEL_DIR + filename, Model.class);
  }

  @NonNull
  public Texture getTexture(@NonNull String path) {
    return assetManager.get(GameScreenAssetPaths.TEXTURE_DIR + path);
  }

  @NonNull
  public Skin getSkin(@NonNull String path) {
    return assetManager.get(path);
  }

  private <T> void loadDirectory(
      @NonNull String path,
      @NonNull String suffix,
      @NonNull Class<T> assetType
  ) {
    var assets = Gdx.files.internal(path).list(suffix);
    var directiories = Gdx.files.internal(path).list();
    for (FileHandle asset : assets) {
      assetManager.load(asset.path(), assetType);
    }
    for (FileHandle directiory : directiories) {
      if (directiory.isDirectory()) {
        loadDirectory(directiory.path(), suffix, assetType);
      }
    }
  }

  private void loadModels() {
    log.info("loading models");
    loadDirectory(GameScreenAssetPaths.MODEL_DIR, ".g3db", Model.class);
    log.info("loaded models (first instance)");
  }

  // we can control there the order of loading textures what is important with transparency
  private void loadTextures() {
    loadDirectory(GameScreenAssetPaths.FIELD_TEXTURE_DIR, ".png", Texture.class);
    loadDirectory(GameScreenAssetPaths.SUBFIELDS_TEXTURE_DIR, ".png", Texture.class);
    loadDirectory(GameScreenAssetPaths.UNITS_TEXTURE_DIR, ".png", Texture.class);
    loadDirectory(GameScreenAssetPaths.BUILDINGS_TEXTURE_DIR, ".png", Texture.class);
    loadDirectory(GameScreenAssetPaths. MATERIALS_TEXTURE_DIR, ".png", Texture.class);
    loadDirectory(GameScreenAssetPaths.TECHNOLOGIES_TEXTURE_DIR, ".png", Texture.class);
    loadDirectory(GameScreenAssetPaths.TEXTURE_DIR, ".png", Texture.class);
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
