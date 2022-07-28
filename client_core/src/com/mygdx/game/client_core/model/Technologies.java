package com.mygdx.game.client_core.model;

import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
@Log
public class Technologies {

  private final List<Integer> technologies;

  @Inject
  public Technologies() {
    this.technologies = new ArrayList<>();
  }

  public void saveTechnology(int entity) {
    log.info("Save technology with id: " + entity);
    technologies.add(entity);
  }

  public List<Integer> getAllTechnologies() {
    return technologies;
  }


}
