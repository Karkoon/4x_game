package com.mygdx.game.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.game.assets.assetloaders.ArrayLoader;
import com.mygdx.game.assets.assetloaders.JsonLoader;
import com.mygdx.game.config.FieldConfig;
import com.mygdx.game.config.GameConfigs;
import com.mygdx.game.config.UnitConfig;
import lombok.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

import static com.mygdx.game.assets.assetloaders.ArrayLoader.ArrayLoaderParameter;

@Singleton
public class Assets implements Disposable {

  @NonNull
  private final AssetManager assetManager;

  @NonNull
  private final GameConfigs gameConfigs = new GameConfigs();

  @Inject
  public Assets() {
    this.assetManager = new AssetManager();
    initCustomLoaders();
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private void initCustomLoaders() {
    assetManager.setLoader(Array.class, new ArrayLoader(new InternalFileHandleResolver()));
    assetManager.setLoader(FieldConfig.class, new JsonLoader<>(new InternalFileHandleResolver()));
    assetManager.setLoader(UnitConfig.class, new JsonLoader<>(new InternalFileHandleResolver()));
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public void loadConfigs() {
    assetManager.load(AssetPaths.FIELD_CONFIG_DIR, Array.class,
        new ArrayLoaderParameter(FieldConfig.class, "json"));
    assetManager.load(AssetPaths.UNIT_CONFIG_DIR, Array.class,
        new ArrayLoaderParameter(UnitConfig.class, "json"));
    assetManager.finishLoading();

    populateGameConfigs();
  }

  public void loadAssets() {
    loadModels();
    loadTextures();
    assetManager.finishLoading();
  }

  @NonNull
  public Model getModel(@NonNull String filename) {
    return assetManager.get(AssetPaths.MODEL_DIR + filename);
  }

  @NonNull
  public Texture getTexture(@NonNull String path) {
    return assetManager.get(AssetPaths.TEXTURE_DIR + path);
  }

  @NonNull
  public GameConfigs getGameConfigs() {
    return gameConfigs;
  }

  @Override
  public void dispose() {
    assetManager.dispose();
  }

  private void loadModels() {
    loadDirectory(AssetPaths.MODEL_DIR, ".g3db", Model.class);
  }

  private void loadTextures() {
    loadDirectory(AssetPaths.TEXTURE_DIR, ".png", Texture.class);
    assetManager.load(AssetPaths.DEMO_TEXTURE_PATH, Texture.class);
  }

  private <T> void loadDirectory(@NonNull String path, @NonNull String suffix, Class<T> assetType) {
    var dir = Gdx.files.internal(path).list(suffix);
    for (var i = 0; i < dir.length; i++) {
      assetManager.load(dir[i].path(), assetType);
    }
  }

  private void populateGameConfigs() {
    gameConfigs.putAll(FieldConfig.class, assetManager.getAll(FieldConfig.class, new Array<>()));
    gameConfigs.putAll(UnitConfig.class, assetManager.getAll(UnitConfig.class, new Array<>()));
  }
}
