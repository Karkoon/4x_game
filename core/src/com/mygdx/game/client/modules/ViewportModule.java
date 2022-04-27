package com.mygdx.game.client.modules;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import dagger.Module;
import dagger.Provides;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Singleton;
import java.util.logging.Level;

@Module
@Log
public class ViewportModule {
  @Provides
  @Singleton
  public @NonNull Viewport providesViewport() {
    log.log(Level.INFO, "provided viewport");
    Camera camera = new PerspectiveCamera(66, 300, 300);
    camera.near = 1f;
    camera.far = 300f;
    return new ExtendViewport(300, 300, camera);
  }
}
