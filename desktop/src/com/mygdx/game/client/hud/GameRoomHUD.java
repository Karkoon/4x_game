package com.mygdx.game.client.hud;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.assets.MenuScreenAssetPaths;
import com.mygdx.game.assets.MenuScreenAssets;
import com.mygdx.game.client.di.StageModule;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Named;

@Log
public class GameRoomHUD implements Disposable {

  private final MenuScreenAssets menuScreenAssets;
  private final Stage stage;
  private final Viewport viewport;
  private final Skin menuScreenSkin;

  @Inject
  public GameRoomHUD(
      @Named(StageModule.SCREEN_STAGE) Stage stage,
      MenuScreenAssets menuScreenAssets,
      Viewport viewport
  ) {
    this.stage = stage;
    this.viewport = viewport;
    this.menuScreenAssets = menuScreenAssets;

    menuScreenSkin = menuScreenAssets.getSkin(MenuScreenAssetPaths.SKIN);

    prepareHudSceleton();
  }

  public void act(float delta) {
    stage.act(delta);
  }

  public void draw() {
    stage.draw();
  }

  public void dispose (){
    stage.dispose();
  }

  public void prepareHudSceleton() {
    stage.clear();
  }
}
