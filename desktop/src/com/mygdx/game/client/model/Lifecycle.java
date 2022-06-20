package com.mygdx.game.client.model;

import lombok.extern.java.Log;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Log
public class Lifecycle {

  private final List<LifecycleObserver> observerList = new ArrayList<>();
  private State currentState = State.SETUP;

  @Inject
  public Lifecycle() {

  }

  public void changeState(State state) {
    if (!currentState.isBeforeOf(state)) {
      log.warning("potential invalid state change from " + currentState + " to " + state);
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
