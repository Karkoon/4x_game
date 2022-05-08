package com.mygdx.game.client.service;

import lombok.extern.java.Log;

import java.util.UUID;
import java.util.function.Function;

@Log
public class LocalTurnService implements TurnService {

  private TurnToken activeToken = null;

  @Override
  public TurnToken peekToken() {
    log.info("peeked token");
    return activeToken;
  }

  @Override
  public void obtainToken(Function<TurnToken, Void> onSuccess, Function<Throwable, Void> onError) {
    activeToken = generateNewToken();
    log.info("obtained token");
    onSuccess.apply(activeToken);
  }

  @Override
  public void releaseToken(TurnToken token) {
    if (activeToken.equals(token)) {
      activeToken = null;
    } else {
      log.info("wrong token tried to be released");
    }
  }

  private TurnToken generateNewToken() {
    return new TurnToken(UUID.randomUUID().toString());
  }

}
