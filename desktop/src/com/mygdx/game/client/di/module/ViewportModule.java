package com.mygdx.game.client.di.module;

import com.badlogic.gdx.graphics.PerspectiveCamera;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.client.di.scope.GameScreenScope;
import dagger.Module;
import dagger.Provides;
import lombok.NonNull;
import lombok.extern.java.Log;

import java.util.logging.Level;

@Module
@Log
public class ViewportModule {
  @Provides
  @GameScreenScope
  public @NonNull Viewport providesViewport() {
    log.log(Level.INFO, "provided viewport");
    var camera = new PerspectiveCamera(66, 300, 300);
    camera.near = 500f;
    camera.far = 700f;
    return new ExtendViewport(300, 300, camera);
  }
}
