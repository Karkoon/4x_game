package com.mygdx.game.client.service;

import com.mygdx.game.client.util.Callback;

public interface SyncListener<T> {
  void handle(Callback<T> result, Callback<Throwable> error);
}
