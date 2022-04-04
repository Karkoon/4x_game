package com.mygdx.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.utils.ScreenUtils;
import com.mygdx.game.client.screen.GameScreen;
import dagger.Lazy;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

@Log
@Singleton
public class MyGdxGame extends Game {

    private final Lazy<GameScreen> gameScreen;

    @Inject
    MyGdxGame(@NonNull Lazy<GameScreen> gameScreen) {
        this.gameScreen = gameScreen;
    }

    @Override
    public void create() {
        changeToGameScreen();
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.5f, 0.5f, 0.5f, 1, true);
        super.render();
    }

    private void changeToGameScreen() {
        setScreen(gameScreen.get());
    }
}
