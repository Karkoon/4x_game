package com.mygdx.game.client.model;

import lombok.extern.java.Log;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Log
public class Lifecycle {

  @Inject
  public Lifecycle() {

  }

  private State currentState = State.SETUP;
  private final List<LifecycleObserver> observerList = new ArrayList<>();

  public void changeState(State state) {
    if (!currentState.isBeforeOf(state)) {
      throw new IllegalArgumentException("States should go in the right order defined in the enum.");
    }
    log.info("changed lifecycle state to " + state);
    currentState = state;
    notifyObservers(state);
  }

  private void notifyObservers(State state) {
    for (int i = 0; i < observerList.size(); i++) {
      observerList.get(i).onStateChanged(state, this);
    }
  }

  public void subscribe(LifecycleObserver observer) {
    observerList.add(observer);
  }

  public void unsubscribe(LifecycleObserver observer) {
    observerList.remove(observer);
  }

  public enum State {
    SETUP, START, IN_PROGRESS, END;

    private boolean isBeforeOf(State next) {
      return this.ordinal() + 1 == next.ordinal()
          || (this.ordinal() == State.values().length - 1 && next.ordinal() == 0);
    }
  }

  public interface LifecycleObserver {
    void onStateChanged(State state, Lifecycle lifecycle);
  }
}
