package com.mygdx.game.core.util;

import com.badlogic.gdx.utils.Disposable;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

public class CompositeDisposable implements Disposable {
  private final List<Disposable> disposableList = new ArrayList<>();

  public void addDisposable(@NonNull Disposable disposable) {
    this.disposableList.add(disposable);
  }

  public void removeDisposable(@NonNull Disposable disposable) {
    this.disposableList.remove(disposable);
  }

  @Override
  public void dispose() {
    for (Disposable disposable : disposableList) {
      disposable.dispose();
    }
  }
}
