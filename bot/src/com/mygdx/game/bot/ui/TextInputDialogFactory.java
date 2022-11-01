package com.mygdx.game.bot.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.mygdx.game.assets.MenuScreenAssetPaths;
import com.mygdx.game.assets.MenuScreenAssets;

import javax.inject.Inject;

public class TextInputDialogFactory {

  private final MenuScreenAssets assets;

  @Inject
  public TextInputDialogFactory(
      MenuScreenAssets assets
  ) {
    this.assets = assets;
  }

  public Dialog create(
      String title,
      String prompt,
      String positiveText,
      String negativeText,
      TextInputDialog.TextInputProcess positiveAction,
      Runnable negativeAction
  ) {
    return new TextInputDialog(
        title, prompt,
        assets.getSkin(MenuScreenAssetPaths.SKIN),
        positiveText, negativeText,
        positiveAction, negativeAction
    );
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
