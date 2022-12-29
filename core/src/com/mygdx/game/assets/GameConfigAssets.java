package com.mygdx.game.assets;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.utils.Array;
import com.mygdx.game.assets.assetloaders.ArrayLoader;
import com.mygdx.game.assets.assetloaders.JsonLoader;
import com.mygdx.game.config.BuildingConfig;
import com.mygdx.game.config.CivilizationConfig;
import com.mygdx.game.config.Config;
import com.mygdx.game.config.FieldConfig;
import com.mygdx.game.config.GameConfigs;
import com.mygdx.game.config.MapTypeConfig;
import com.mygdx.game.config.SubFieldConfig;
import com.mygdx.game.config.TechnologyConfig;
import com.mygdx.game.config.UnitConfig;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.List;

@Log
@Singleton
public class GameConfigAssets {

  private final AssetManager assetManager;
  @NonNull
  private final GameConfigs gameConfigs;

  private final List<Class<? extends Config>> configClasses =
    List.of(
      BuildingConfig.class,
      CivilizationConfig.class,
      FieldConfig.class,
      MapTypeConfig.class,
      SubFieldConfig.class,
      TechnologyConfig.class,
      UnitConfig.class
    );

  @Inject
  public GameConfigAssets(
      AssetManager assetManager,
      GameConfigs gameConfigs
  ) {
    this.assetManager = assetManager;
    this.gameConfigs = gameConfigs;
    initCustomLoaders();
  }

  public void loadAssetsAsync() {
    loadConfigs();
  }

  @NonNull
  public GameConfigs getGameConfigs() {
    if (gameConfigs.size() == 0) {
      populateGameConfigs();
    }
    return gameConfigs;
  }

  public void loadAssetsSync() {
    loadConfigs();
    assetManager.finishLoading();
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private void initCustomLoaders() {
    assetManager.setLoader(Array.class, new ArrayLoader(new InternalFileHandleResolver()));
    for (var aClass : configClasses) {
      setJsonLoader(aClass);
    }
  }

  private void setJsonLoader(Class<?> type) {
    assetManager.setLoader(type, new JsonLoader<>(new InternalFileHandleResolver()));
  }

  private void loadConfigs() {
    loadArrayAsset(GameConfigAssetPaths.BUILDING_CONFIG_DIR, BuildingConfig.class);
    loadArrayAsset(GameConfigAssetPaths.CIVILIZATION_CONFIG_DIR, CivilizationConfig.class);
    loadArrayAsset(GameConfigAssetPaths.FIELD_CONFIG_DIR, FieldConfig.class);
    loadArrayAsset(GameConfigAssetPaths.MAP_TYPE_CONFIG_DIR, MapTypeConfig.class);
    loadArrayAsset(GameConfigAssetPaths.SUB_FIELD_CONFIG_DIR, SubFieldConfig.class);
    loadArrayAsset(GameConfigAssetPaths.TECHNOLOGY_CONFIG_DIR, TechnologyConfig.class);
    loadArrayAsset(GameConfigAssetPaths.UNIT_CONFIG_DIR, UnitConfig.class);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  private void loadArrayAsset(String path, Class<?> type) {
    assetManager.load(path, Array.class, new ArrayLoader.ArrayLoaderParameter(type, "json"));
  }

  private void populateGameConfigs() {
    log.info("populated game configs");
    for (var configClass : configClasses) {
      gameConfigs.putAll(assetManager.getAll(configClass, new Array<>()));
    }
  }
}
