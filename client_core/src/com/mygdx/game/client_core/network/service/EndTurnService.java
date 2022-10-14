package com.mygdx.game.client_core.network.service;

import com.mygdx.game.client_core.network.MessageSender;
import dagger.Lazy;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class EndTurnService {

  private final Lazy<MessageSender> sender;

  @Inject
  public EndTurnService(
      Lazy<MessageSender> sender
  ) {
    this.sender = sender;
  }

  public void endTurn() {
    log.info("end turn request send");
    sender.get().send("end_turn");
  }
}
