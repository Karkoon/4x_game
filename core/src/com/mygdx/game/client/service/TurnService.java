package com.mygdx.game.client.service;

import com.mygdx.game.client.util.Callback;
import lombok.NonNull;

public interface TurnService {
  TurnToken peekToken();

  void obtainToken(@NonNull Callback<TurnToken> onSuccess,
                   @NonNull Callback<Throwable> onError);

  void releaseToken(@NonNull TurnToken token,
                    @NonNull Callback<Boolean> onSuccess,
                    @NonNull Callback<Throwable> onError);
}
