package com.mygdx.game.client.util;

import com.badlogic.gdx.utils.Null;

public interface Callback<T> {

  /**
   * @param result of async work
   */
  void handle(@Null T result);
}
