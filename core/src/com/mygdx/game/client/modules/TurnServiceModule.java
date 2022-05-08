package com.mygdx.game.client.modules;

import com.mygdx.game.client.service.LocalTurnService;
import com.mygdx.game.client.service.TurnService;
import dagger.Module;
import dagger.Provides;
import lombok.extern.java.Log;

import javax.inject.Singleton;

@Module
@Log
public class TurnServiceModule {

  @Provides
  @Singleton
  public TurnService providesTurnService() {
    log.info("provided TurnService");
    return new LocalTurnService();
  }
}
