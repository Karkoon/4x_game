package com.mygdx.game.client_core.network.service;

import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.network.MessageSender;

import javax.inject.Inject;

@GameInstanceScope
public class GameInterruptedService {

  private final MessageSender sender;

  @Inject
  public GameInterruptedService(
      MessageSender sender
  ) {
    this.sender = sender;
  }

  public void sendInterruptNotification() {
    sender.send("interrupt");
  }

}
