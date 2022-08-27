package com.mygdx.game.client.model;

import com.badlogic.gdx.math.collision.Ray;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import lombok.Data;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Data
@GameInstanceScope
@Log
public class ClickInput {
  private Ray value = null;
  private boolean handled = true;

  @Inject
  public ClickInput() {
    super();
  }

  public void setValue(Ray value) {
    if (handled) {
      log.info("value set: " + value + " thread: " + Thread.currentThread().getName());
      this.value = value;
      this.handled = false;
    }
  }

  public void clear() {
    value = null;
    handled = true;
  }
}
