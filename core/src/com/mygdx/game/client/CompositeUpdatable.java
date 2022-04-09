package com.mygdx.game.client;

import lombok.NonNull;

import java.util.ArrayList;
import java.util.List;

public class CompositeUpdatable implements Updatable {

  private final List<Updatable> updatableList = new ArrayList<>();

  public void addUpdatable(@NonNull Updatable updatable) {
    this.updatableList.add(updatable);
  }

  public void removeUpdatable(@NonNull Updatable updatable) {
    this.updatableList.remove(updatable);
  }

  @Override
  public void update(float delta) {
    for (Updatable updatable : updatableList) {
      updatable.update(delta);
    }
  }

}
