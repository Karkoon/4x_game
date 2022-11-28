package com.mygdx.game.client.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.client.GdxGame;
import com.mygdx.game.client.di.StageModule;
import com.mygdx.game.client.hud.GameRoomScreenHUD;
import com.mygdx.game.client.util.UiElementsCreator;
import com.mygdx.game.client_core.network.service.GameConnectService;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
@Log
public class GameRoomListScreen extends ScreenAdapter {

  private final GameConnectService connectService;
  private final GdxGame game;
  private final GameRoomScreenHUD gameRoomScreenHUD;
  private final Stage stage;
  private final UiElementsCreator uiElementsCreator;

  private String roomName = "defaultRoom";
  private String userName = "defaultUser";

  @Inject
  public GameRoomListScreen(
      GameConnectService connectService,
      GdxGame game,
      GameRoomScreenHUD gameRoomScreenHUD,
      @Named(StageModule.SCREEN_STAGE) Stage stage,
      UiElementsCreator uiElementsCreator
  ) {
    this.connectService = connectService;
    this.game = game;
    this.gameRoomScreenHUD = gameRoomScreenHUD;
    this.stage = stage;
    this.uiElementsCreator = uiElementsCreator;
  }

  @Override
  public void show() {
    stage.clear();

    var title = "Room List";
    var dialog = uiElementsCreator.createDialog(title);
    var table = new Table();

    var roomNameLabel = uiElementsCreator.createDialogLabel("Game room name");
    roomNameLabel.setAlignment(Align.center);
    table.add(roomNameLabel);
    table.row();

    var roomNameField = uiElementsCreator.createDialogTextField(roomName);
    roomNameField.setAlignment(Align.center);
    roomNameField.setTextFieldListener((textField, c) -> roomName = textField.getText());
    table.add(roomNameField);
    table.row();

    var userNameLabel = uiElementsCreator.createDialogLabel("User name");
    userNameLabel.setAlignment(Align.center);
    table.add(userNameLabel);
    table.row();

    var userNameField = uiElementsCreator.createDialogTextField(userName);
    userNameField.setAlignment(Align.center);
    userNameField.setTextFieldListener((textField, c) -> userName = textField.getText());
    table.add(userNameField);
    table.pad(20);
    dialog.getContentTable().add(table);

    var joinButton = uiElementsCreator.createDialogButton("JOIN");
    joinButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        joinRoom();
      }
    });
    dialog.button(joinButton);

    var leaveButton = uiElementsCreator.createDialogButton("LEAVE");
    leaveButton.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        backToMenu();
      }
    });
    dialog.button(leaveButton);
    dialog.pack();
    dialog.show(stage);
    Gdx.input.setInputProcessor(stage);
    super.show();
  }

  @Override
  public void render(float delta) {
    super.render(delta);
    stage.act(delta);
    stage.draw();
  }

  @Override
  public void resize(int width, int height) {
    super.resize(width, height);
    stage.getViewport().update(width, height, true);
  }

  private void joinRoom() {
    connectService.connect(roomName, userName, "NOT_BOT");
    game.changeToGameRoomScreen();
  }

  private void backToMenu() {
    game.changeToMenuScreen();
  }

}
