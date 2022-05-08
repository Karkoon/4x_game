package com.mygdx.game.client.service;

import com.mygdx.game.client.model.GameState;
import lombok.NonNull;

import java.util.function.UnaryOperator;

public class LocalVerifier implements GameStateVerifier {

  private final TurnService turnService;
  private final GameState localGameState = new GameState();

  public LocalVerifier(@NonNull TurnService turnService) {
    this.turnService = turnService;
  }

  @Override
  public void verify(@NonNull GameState gameState,
                     @NonNull TurnToken token,
                     @NonNull UnaryOperator<Boolean> onSuccess,
                     UnaryOperator<Throwable> onError) {
    if (!turnService.peekToken().equals(token)) {
      onSuccess.apply(false);
    }
    try {
      onSuccess.apply(validate(gameState));
    } catch (Exception e) {
      onError.apply(e);
    }

  }

  private boolean validate(GameState newGameState) {
    return true;
  }
}
