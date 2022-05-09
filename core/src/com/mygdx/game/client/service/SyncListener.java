package com.mygdx.game.client.service;

import lombok.NonNull;

public interface SyncListener<T> {
  void handle(Result<T> result);

  class Result<T> {

    final T data;
    final boolean isSuccessful;

    private Result(boolean isSuccessful, T data) {
      this.data = data;
      this.isSuccessful = isSuccessful;
    }

    public Result<T> ok(@NonNull T data) {
      return new Result<>(true, data);
    }

    public Result<T> error() {
      return new Result<>(false, null);
    }
  }
}
