package com.mygdx.game.client_core.network.service;

import com.mygdx.game.client_core.network.MessageSender;
import dagger.Lazy;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
public class GameStartService {

  private final Lazy<MessageSender> sender;

  @Inject
  public GameStartService(
      Lazy<MessageSender> sender
  ) {
    this.sender = sender;
  }

  public void startGame(int width, int height, int mapType) {
    log.info("start game request sent");
    sender.get().send(String.format("start:%d:%d:%d", width, height, mapType));
  }
}
