package com.mygdx.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.mygdx.assets.assetloaders.ArrayLoader;
import com.mygdx.assets.assetloaders.ArrayLoader.ArrayLoaderParameter;
import com.mygdx.assets.assetloaders.JsonLoader;
import com.mygdx.config.EntityConfig;
import com.mygdx.config.FieldConfig;
import com.mygdx.config.GameConfigs;
import com.mygdx.config.UnitConfig;
import lombok.NonNull;

import javax.inject.Inject;
import javax.inject.Singleton;

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
    loadModels(getGameConfigs().getAll());
    loadTextures();
    assetManager.finishLoading();
  }

  @NonNull
  public Model getModel(@NonNull EntityConfig config) {
    return assetManager.get(AssetPaths.MODEL_DIR + config.getModelPath());
  }

  @NonNull
  public Texture getTexture(@NonNull String path) {
    return assetManager.get(path, Texture.class);
  }

  @NonNull
  public GameConfigs getGameConfigs() {
    return gameConfigs;
  }

  @Override
  public void dispose() {
    assetManager.dispose();
  }

  private void loadModels(@NonNull Array<EntityConfig> entityConfigs) {
    for (var i = 0; i < entityConfigs.size; i++) {
      var entityConfig = entityConfigs.get(i);
      assetManager.load(AssetPaths.MODEL_DIR + entityConfig.getModelPath(), Model.class);
    }
  }

  private void loadTextures() {
    assetManager.load(AssetPaths.DEMO_TEXTURE_PATH, Texture.class);
  }

  private void populateGameConfigs() {
    gameConfigs.putAll(FieldConfig.class, assetManager.getAll(FieldConfig.class, new Array<>()));
    gameConfigs.putAll(UnitConfig.class, assetManager.getAll(UnitConfig.class, new Array<>()));
  }
}
