package com.mygdx.game.client.service;

import com.mygdx.game.client.util.Callback;
import lombok.extern.java.Log;

import java.util.UUID;

@Log
public class LocalTurnService implements TurnService {

  private TurnToken activeToken = null;

  @Override
  public TurnToken peekToken() {
    log.info("peeked token");
    return activeToken;
  }

  @Override
  public void obtainToken(Callback<TurnToken> onSuccess, Callback<Throwable> onError) {
    activeToken = generateNewToken();
    log.info("obtained token");
    onSuccess.handle(activeToken);
  }

  @Override
  public void releaseToken(TurnToken token, Callback<Boolean> onSuccess, Callback<Throwable> onError) {
    if (token.equals(activeToken)) {
      activeToken = null;
      onSuccess.handle(true);
    } else {
      onSuccess.handle(false);
    }
  }

  private TurnToken generateNewToken() {
    return new TurnToken(UUID.randomUUID().toString());
  }
}
