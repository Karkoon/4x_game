package com.mygdx.game.assets;

public final class MenuScreenAssetPaths {

  public static final String ASSETS_PATH = "./";
  public static final String TEXTURE_DIR = ASSETS_PATH + "textures/";
  public static final String PLANET = TEXTURE_DIR + "planet.png";
  public static final String NEBULA = TEXTURE_DIR + "noise.png";
  public static final String STARS = TEXTURE_DIR + "stars.png";

  public static final String SKIN = ASSETS_PATH + "sgx/skin/sgx-ui.json";

  public static final String SHADER_DIR = ASSETS_PATH + "shaders/";
  public static final String STARS_SHADER = SHADER_DIR + "stars.frag";
  public static final String NEBULA_SHADER = SHADER_DIR + "nebula.frag";
  public static final String PLANET_SHADER = SHADER_DIR + "mask.frag";

  private MenuScreenAssetPaths() {
  }
}
