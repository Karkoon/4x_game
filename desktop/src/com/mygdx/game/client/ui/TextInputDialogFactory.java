package com.mygdx.game.client.ui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.assets.MenuScreenAssetPaths;
import com.mygdx.game.assets.MenuScreenAssets;
import com.mygdx.game.client.di.Names;
import com.mygdx.game.client.util.UiElementsCreator;

import javax.inject.Inject;
import javax.inject.Named;

public class TextInputDialogFactory {

  private final Stage stage;
  private final MenuScreenAssets assets;

  @Inject
  public TextInputDialogFactory(
      MenuScreenAssets assets,
      @Named(Names.GAME_ROOM_LIST_SCREEN) Stage stage
  ) {
    this.assets = assets;
    this.stage = stage;
  }

  public void createAndShow(
      String title,
      String prompt,
      String positiveText,
      String negativeText,
      TextInputDialog.TextInputProcess positiveAction,
      Runnable negativeAction
  ) {
    var dialog = new TextInputDialog(
        title, prompt,
        assets.getSkin(MenuScreenAssetPaths.SKIN),
        positiveText, negativeText,
        positiveAction, negativeAction
    );
    dialog.show(stage);
  }

  private static class TextInputDialog extends Dialog {

    private final Runnable negativeAction;
    private final TextInputProcess positiveAction;

    private final TextField field;

    public TextInputDialog(
        String title,
        String prompt,
        Skin skin,
        String positiveText,
        String negativeText,
        TextInputProcess positiveAction,
        Runnable negativeAction
    ) {
      super(title, skin);
      this.positiveAction = positiveAction;
      this.negativeAction = negativeAction;
      setMovable(false);
      setResizable(false);
      pad(50);
      getTitleLabel().setAlignment(Align.center);

      field = new TextField(prompt, skin);
      field.setAlignment(Align.center);
      field.setMaxLength(12);
      getContentTable().add(field).pad(50);

      button(positiveText, ChosenButton.POSITIVE);
      button(negativeText, ChosenButton.NEGATIVE);
      pack();
    }

    @Override
    protected void result(Object object) {
      var button = (ChosenButton) object;
      switch (button) {
        case POSITIVE -> positiveAction.process(field.getText());
        case NEGATIVE -> negativeAction.run();
        default -> throw new IllegalArgumentException();
      }
    }

    private enum ChosenButton {
      POSITIVE, NEGATIVE
    }

    public interface TextInputProcess {
      void process(String string);
    }
  }
}
