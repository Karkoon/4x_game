package com.mygdx.game.client.service;

import java.util.function.Function;

public interface TurnService {
  TurnToken peekToken();

  void obtainToken(Function<TurnToken, Void> onSuccess,
                   Function<Throwable, Void> onError);

  void releaseToken(TurnToken token);
}
