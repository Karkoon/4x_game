package com.mygdx.game.client_core.di;

import dagger.Module;
import dagger.Provides;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.schedulers.Schedulers;

import javax.inject.Named;
import javax.inject.Singleton;

@Module
public class MainSchedulerModule {

  @Singleton
  @Provides
  @Named(CoreNames.MAIN_THREAD)
  public Scheduler mainThread() {
    return Schedulers.newThread();
  }
}
