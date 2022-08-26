package com.mygdx.game.client.di;

import javax.inject.Qualifier;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Qualifier
@Retention(RetentionPolicy.CLASS)
public @interface GameScreen {
}
