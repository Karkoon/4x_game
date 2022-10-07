package com.mygdx.game.server.network.gameinstance.handlers;

import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.network.gameinstance.services.ResearchTechnologyService;

import javax.inject.Inject;

@GameInstanceScope
public class ResearchHandler extends EntityCommandHandler {

  private final ResearchTechnologyService researchTechnologyService;

  @Inject
  public ResearchHandler(
      ResearchTechnologyService researchTechnologyService
  ) {
    this.researchTechnologyService = researchTechnologyService;
  }

  public void handle(String[] commands, Client client) {
    int entityId = Integer.parseInt(commands[1]);
    researchTechnologyService.researchTechnology(entityId, client);
  }
}
