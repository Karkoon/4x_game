package com.mygdx.game.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Array;
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
public class GameScreenAssets {

  @NonNull
  private final AssetManager assetManager;

  @NonNull
  private final GameConfigs gameConfigs = new GameConfigs();

  @Inject
  public GameScreenAssets(@NonNull AssetManager assetManager) {
    this.assetManager = assetManager;
    initCustomLoaders();
  }

  public void loadAssetsAsync() {
    loadConfigs();
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

  @NonNull
  public GameConfigs getGameConfigs() {
    if (gameConfigs.size() == 0) {
      populateGameConfigs();
    }
    return gameConfigs;
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
    assetManager.load(GameScreenAssetPaths.DEMO_TEXTURE_PATH, Texture.class);
  }

  private void loadSkin() {
    assetManager.load(GameScreenAssetPaths.DIALOG_SKIN, Skin.class);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private void loadConfigs() {
    assetManager.load(GameScreenAssetPaths.FIELD_CONFIG_DIR, Array.class,
        new ArrayLoaderParameter(FieldConfig.class, "json"));
    assetManager.load(GameScreenAssetPaths.UNIT_CONFIG_DIR, Array.class,
        new ArrayLoaderParameter(UnitConfig.class, "json"));
  }

  private void populateGameConfigs() {
    gameConfigs.putAll(FieldConfig.class, assetManager.getAll(FieldConfig.class, new Array<>()));
    gameConfigs.putAll(UnitConfig.class, assetManager.getAll(UnitConfig.class, new Array<>()));
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private void initCustomLoaders() {
    assetManager.setLoader(Array.class, new ArrayLoader(new InternalFileHandleResolver()));
    assetManager.setLoader(FieldConfig.class, new JsonLoader<>(new InternalFileHandleResolver()));
    assetManager.setLoader(UnitConfig.class, new JsonLoader<>(new InternalFileHandleResolver()));
  }
}
