package com.mygdx.game.client_core.model;

import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import lombok.Data;

import javax.inject.Inject;

@Data
@GameInstanceScope
public class ChangesApplied {

  @Inject
  public ChangesApplied() {
    super();
  }

  private Runnable changesAppliedListener = () -> {};
}
