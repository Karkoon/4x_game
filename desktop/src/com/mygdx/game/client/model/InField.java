package com.mygdx.game.client.model;

import lombok.Data;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Data
@Singleton
@Log
public class InField {

  @Inject
  public InField() {
    super();
    log.info("ONLY ONCE");
  }
  private boolean inField = false;
}
