package com.mygdx.game.client.modules;

import com.mygdx.game.client.service.GameStateVerifier;
import com.mygdx.game.client.service.LocalVerifier;
import com.mygdx.game.client.service.TurnService;
import dagger.Module;
import dagger.Provides;
import lombok.extern.java.Log;

import javax.inject.Singleton;

@Module
@Log
public class GameStateVerifierModule {

  @Provides
  @Singleton
  public GameStateVerifier providesGameStateVerifier(TurnService turnService) {
    log.info("provided TurnService");
    return new LocalVerifier(turnService);
  }
}
