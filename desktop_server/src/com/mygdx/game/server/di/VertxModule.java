package com.mygdx.game.server.di;

import dagger.Module;
import dagger.Provides;
import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;

import javax.inject.Singleton;

@Module
public class VertxModule {

  @Provides
  @Singleton
  public Vertx provideVertx() {
    return Vertx.vertx();
  }

  @Provides
  @Singleton
  public HttpServer provideHttpServer(Vertx vertx) {
    return vertx.createHttpServer();
  }
}
