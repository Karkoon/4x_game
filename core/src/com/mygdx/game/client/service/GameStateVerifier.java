package com.mygdx.game.client.service;

import com.mygdx.game.client.model.GameState;
import lombok.NonNull;

import java.util.function.UnaryOperator;

public interface GameStateVerifier {

  void verify(@NonNull GameState gameState, @NonNull TurnToken token,
              @NonNull UnaryOperator<Boolean> onSuccess, UnaryOperator<Throwable> onError);

}
