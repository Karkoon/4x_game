package com.mygdx.game.assets;

public final class GameConfigAssetPaths {
  public static final String ASSETS_PATH = "./";
  public static final String ENTITY_CONFIG_DIR = ASSETS_PATH + "entity_configs/";
  public static final String BUILDING_CONFIG_DIR = ENTITY_CONFIG_DIR + "buildings/";
  public static final String CIVILIZATION_CONFIG_DIR = ENTITY_CONFIG_DIR + "civilizations/";
  public static final String FIELD_CONFIG_DIR = ENTITY_CONFIG_DIR + "fields/";
  public static final String MAP_TYPE_CONFIG_DIR = ASSETS_PATH + "maptype_configs/";
  public static final String SUB_FIELD_CONFIG_DIR = ENTITY_CONFIG_DIR + "subfields/";
  public static final String TECHNOLOGY_CONFIG_DIR = ENTITY_CONFIG_DIR + "technologies/";
  public static final String UNIT_CONFIG_DIR = ENTITY_CONFIG_DIR + "units/";

  private GameConfigAssetPaths() {
  }
}
