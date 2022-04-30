package com.mygdx.game.client.screen;

import com.badlogic.ashley.core.ComponentMapper;
import com.badlogic.ashley.core.Entity;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.client.component.FieldComponent;
import lombok.extern.java.Log;

import java.util.logging.Level;

@Log
public class ChooseUnitFieldDialog {

  private static final ComponentMapper<FieldComponent> fieldComponentMapper = ComponentMapper.getFor(FieldComponent.class);
  private static final Skin uiSkin = new Skin(Gdx.files.internal("dialog/custom/uiskin.json"));

  public static Dialog customDialog = null;

  public static void createCustomDialog(Stage parentStage, Entity entity) {
    if (customDialog != null) {
      customDialog.hide();
      customDialog = null;
    }

    FieldComponent fieldComponent = fieldComponentMapper.get(entity);

    customDialog = new Dialog("Choose", uiSkin) {
      @Override
      protected void result(Object object) {
        System.out.println("HEAJ");
        super.result(object);
        if (object.equals("field")) {
          log.log(Level.INFO, "Selected field.");
        } else if (object.equals("unit")) {
          log.log(Level.INFO, "Selected field.");
        }
      }
    };

    customDialog.text("Choose on of the following:");
    customDialog.button("Field" + fieldComponent.getName(), "field");
    customDialog.show(parentStage);
  }

}
