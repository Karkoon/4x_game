package com.mygdx.game.client_core.di;

import dagger.Module;
import dagger.Provides;
import io.reactivex.rxjava3.core.Scheduler;

import javax.inject.Named;
import javax.inject.Singleton;

@Module
public class MainSchedulerModule {

  private final Scheduler mainScheduler;

  public MainSchedulerModule(Scheduler scheduler) {
    mainScheduler = scheduler;
  }

  @Singleton
  @Provides
  @Named(CoreNames.MAIN_THREAD)
  public Scheduler mainThread() {
    return mainScheduler;
  }
}
