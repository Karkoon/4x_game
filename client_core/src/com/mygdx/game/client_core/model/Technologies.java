package com.mygdx.game.client_core.model;

import com.badlogic.gdx.utils.IntArray;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@GameInstanceScope
@Log
public class Technologies {

  private final IntArray allTechnologies;

  @Inject
  public Technologies() {
    this.allTechnologies = new IntArray();
  }

  public void saveTechnology(int entity) {
    log.info(Thread.currentThread().getName() + " " + Thread.currentThread().getId() + " " + "Save technology with entity id: " + entity);
    allTechnologies.add(entity);
  }

  public IntArray getAllTechnologies() {
    return allTechnologies;
  }

}
