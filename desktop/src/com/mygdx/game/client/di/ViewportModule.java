package com.mygdx.game.client.di;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import dagger.Module;
import dagger.Provides;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Named;
import javax.inject.Singleton;
import java.util.logging.Level;

@Module
@Log
public class ViewportModule {
  @Provides @Named("perspective")
  @Singleton
  public @NonNull Viewport providesPerspectiveViewport() {
    log.log(Level.INFO, "Provided Perspective viewport");
    var camera = new PerspectiveCamera(100, 500, 500);
    camera.near = 500f;
    camera.far = 700f;
    return new ExtendViewport(500, 500, camera);
  }

  @Provides @Named("orthographic")
  @Singleton
  public @NonNull Viewport providesOrthographicViewport() {
    log.log(Level.INFO, "Provided Orthographic viewport");
    var camera = new OrthographicCamera();
    return new ExtendViewport(500, 500, camera);
  }
}
