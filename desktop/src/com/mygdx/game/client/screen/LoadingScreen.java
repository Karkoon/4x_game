package com.mygdx.game.client.screen;

import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.assets.LoadingScreenAssetPaths;
import com.mygdx.game.assets.LoadingScreenAssets;
import com.mygdx.game.assets.MenuScreenAssets;
import com.mygdx.game.client.GdxGame;
import com.mygdx.game.client.ui.actor.LoadingBar;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Log
@Singleton
public class LoadingScreen extends ScreenAdapter {

  private final AssetManager assetManager;
  private final GameConfigAssets gameConfigAssets;
  private final GameScreenAssets gameScreenAssets;
  private final GdxGame gdxGame;
  private final LoadingScreenAssets loadingScreenAssets;
  private final MenuScreenAssets menuScreenAssets;

  private Stage stage;
  private Image logo;
  private Image loadingFrame;
  private Image loadingBarHidden;
  private Image screenBg;
  private Image loadingBg;
  private float startX;
  private float endX;
  private float displayedPercent;
  private LoadingBar loadingBar;

  @Inject
  public LoadingScreen(
      AssetManager assetManager,
      GameScreenAssets gameScreenAssets,
      GameConfigAssets gameConfigAssets,
      GdxGame gdxGame,
      MenuScreenAssets menuScreenAssets,
      LoadingScreenAssets loadingScreenAssets
  ) {
    this.assetManager = assetManager;
    this.gameConfigAssets = gameConfigAssets;
    this.gameScreenAssets = gameScreenAssets;
    this.gdxGame = gdxGame;
    this.loadingScreenAssets = loadingScreenAssets;
    this.menuScreenAssets = menuScreenAssets;
  }

  @Override
  public void show() {
    loadingScreenAssets.loadAssetsSync();
    stage = new Stage(new ScreenViewport());
    getLoadingScreenAssets();
    gameConfigAssets.loadAssetsAsync();
    menuScreenAssets.loadAssetsAsync();
    gameScreenAssets.loadAssetsAsync();
  }

  private void getLoadingScreenAssets() {
    createLoadingBarRelatedImages();
  }

  @Override
  public void resize(int width, int height) {
    stage.getViewport().update(width, height, true);
    screenBg.setSize(width, height);
    positionLoadingBar(width, height);
  }

  @Override
  public void render(float delta) {
    if (assetManager.update()) {
      gdxGame.changeToMenuScreen();
    }
    updateLoadingBar();
    stage.act();
    stage.draw();
  }

  private void updateLoadingBar() {
    displayedPercent = Interpolation.linear.apply(displayedPercent, assetManager.getProgress(), 0.1f);
    loadingBarHidden.setX(startX + endX * displayedPercent);
    loadingBg.setX(loadingBarHidden.getX() + 30);
    loadingBg.setWidth(450 - 450 * displayedPercent);
    loadingBg.invalidate();
  }

  private void positionLoadingBar(float width, float height) {
    logo.setX((width - logo.getWidth()) / 2);
    logo.setY((height - logo.getHeight()) / 2 + 100);

    loadingFrame.setX((stage.getWidth() - loadingFrame.getWidth()) / 2);
    loadingFrame.setY((stage.getHeight() - loadingFrame.getHeight()) / 2);

    loadingBar.setX(loadingFrame.getX() + 15);
    loadingBar.setY(loadingFrame.getY() + 5);

    loadingBarHidden.setX(loadingBar.getX() + 35);
    loadingBarHidden.setY(loadingBar.getY() - 3);

    startX = loadingBarHidden.getX();
    endX = 440;

    loadingBg.setSize(450, 50);
    loadingBg.setX(loadingBarHidden.getX() + 30);
    loadingBg.setY(loadingBarHidden.getY() + 3);
  }

  private void createLoadingBarRelatedImages() {
    var atlas = loadingScreenAssets.getTextureAtlas(LoadingScreenAssetPaths.LOADING_SCREEN_TEXTURE_ATLAS);
    logo = new Image(atlas.findRegion(LoadingScreenAssetPaths.LOGO));
    loadingFrame = new Image(atlas.findRegion(LoadingScreenAssetPaths.LOADING_FRAME));
    loadingBarHidden = new Image(atlas.findRegion(LoadingScreenAssetPaths.LOADING_BAR_HIDDEN));
    screenBg = new Image(atlas.findRegion(LoadingScreenAssetPaths.SCREEN_BACKGROUND));
    loadingBg = new Image(atlas.findRegion(LoadingScreenAssetPaths.LOADING_FRAME_BACKGROUND));
    var anim = new Animation<TextureRegion>(0.05f, atlas.findRegions(LoadingScreenAssetPaths.LOADING_BAR_ANIMATION));
    anim.setPlayMode(Animation.PlayMode.LOOP_REVERSED);
    loadingBar = new LoadingBar(anim);
    stage.addActor(screenBg);
    stage.addActor(loadingBar);
    stage.addActor(loadingBg);
    stage.addActor(loadingBarHidden);
    stage.addActor(loadingFrame);
    stage.addActor(logo);
  }
}
