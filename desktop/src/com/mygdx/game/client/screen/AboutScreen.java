package com.mygdx.game.client.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Window;
import com.mygdx.game.client.GdxGame;
import com.mygdx.game.client.di.StageModule;
import com.mygdx.game.client.ui.decorations.StarBackground;
import com.mygdx.game.client.util.UiElementsCreator;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
@Log
public class AboutScreen extends ScreenAdapter {

  private final GdxGame game;
  private final Stage stage;
  private final UiElementsCreator uiElementsCreator;
  private final StarBackground starBackground;
  private Window authorsWindow;


  @Inject
  public AboutScreen(
      GdxGame game,
      @Named(StageModule.SCREEN_STAGE) Stage stage,
      UiElementsCreator uiElementsCreator,
      StarBackground starBackground
  ) {
    this.game = game;
    this.stage = stage;
    this.uiElementsCreator = uiElementsCreator;
    this.starBackground = starBackground;
  }

  @Override
  public void show() {
    Gdx.input.setInputProcessor(stage);
    prepareSceleton();
  }

  private void prepareSceleton() {
    stage.clear();

    float width = stage.getWidth();
    float height = stage.getHeight();

    this.authorsWindow = uiElementsCreator.createWindow("Author list");
    this.authorsWindow.setMovable(false);
    uiElementsCreator.setActorPosition(this.authorsWindow, (int) (width * 0.25), (int) (height * 0.25));
    uiElementsCreator.setActorWidthAndHeight(this.authorsWindow, (int) (width * 0.5), (int) (height * 0.5));

    var verticalContainer = uiElementsCreator.createVerticalContainer(0, 0, (int) (width * 0.5), (int) (height * 0.5));
    this.authorsWindow.add(verticalContainer);

    var labelFirst = uiElementsCreator.createLabel("Kacper Jankowski", 0, 0);
    var labelSecond = uiElementsCreator.createLabel("Jakub Klimczak", 0, 0);
    var labelThird = uiElementsCreator.createLabel("Przemysław Kolaszyński", 0, 0);
    verticalContainer.addActor(labelFirst);
    verticalContainer.addActor(labelSecond);
    verticalContainer.addActor(labelThird);

    var exit = uiElementsCreator.createActionButton("Exit", this::backToMenu, 0, 0);
    verticalContainer.addActor(exit);

    stage.addActor(authorsWindow);
  }

  public void act(float delta) {
    stage.act(delta);
  }


  @Override
  public void render(float delta) {
    super.render(delta);
    starBackground.update(delta);
    stage.getBatch().begin();
    starBackground.draw(stage.getBatch(), stage.getCamera());
    stage.getBatch().end();

    stage.act(delta);
    stage.getBatch().begin();
    stage.getBatch().end();

    stage.draw();
  }

  @Override
  public void resize(int width, int height) {
    super.resize(width, height);
    starBackground.resize(width, height);
    stage.getViewport().update(width, height, true);
    prepareSceleton();
  }

  private void backToMenu() {
    game.changeToMenuScreen();
  }

}
