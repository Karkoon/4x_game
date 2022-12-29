package com.mygdx.game.client_core.network.service;

import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.network.MessageSender;
import dagger.Lazy;
import lombok.extern.java.Log;

import javax.inject.Inject;

@GameInstanceScope
@Log
public class EndTurnService {

  private final Lazy<MessageSender> messageSender;

  @Inject
  public EndTurnService(
      Lazy<MessageSender> messageSender
  ) {
    this.messageSender = messageSender;
  }

  public void endTurn() {
    log.info("end turn request send");
    messageSender.get().send("end_turn");
  }
}
