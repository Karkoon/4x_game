package com.mygdx.game.client.di;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import dagger.Module;
import dagger.Provides;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.logging.Level;

@Log
@Module
public class ViewportModule {

  @Provides
  @Singleton
  public @NonNull Viewport providesPerspectiveViewport() {
    log.log(Level.INFO, "Provided Perspective viewport");
    var camera = new PerspectiveCamera(100, 500, 500);
    camera.near = 500f;
    camera.far = 700f;
    camera.position.set(0, 600, 0);
    camera.lookAt(0, 0, 0);
    return new ExtendViewport(500, 500, camera);
  }

  public static final String HUD_VIEWPORT = "hud_viewport";

  @Provides
  @Singleton
  @Named(HUD_VIEWPORT)
  public Viewport providesFitViewport() {
    log.log(Level.INFO, "Provided fit viewport");
    var camera = new PerspectiveCamera(100, 500, 500);
    return new FitViewport(500, 500, camera);
  }

}
