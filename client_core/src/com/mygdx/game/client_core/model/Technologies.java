package com.mygdx.game.client_core.model;

import com.badlogic.gdx.utils.IntArray;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
@Log
public class Technologies {

  private final IntArray allTechnologies;

  @Inject
  public Technologies() {
    this.allTechnologies = new IntArray();
  }

  public void saveTechnology(int entity) {
    log.info("Save technology with entity id: " + entity);
    allTechnologies.add(entity);
  }

  public IntArray getAllTechnologies() {
    return allTechnologies;
  }

}
