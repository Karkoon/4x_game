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

  public static void createCustomDialog(Stage parentStage, Entity entity) {
    FieldComponent fieldComponent = fieldComponentMapper.get(entity);

    Dialog customDialog = new Dialog("Choose", uiSkin);
    customDialog.text("Choose on of the following");
    addFieldButton(customDialog, fieldComponent);
    customDialog.show(parentStage);
  }

  private static void addFieldButton(Dialog customDialog, FieldComponent fieldComponent) {
    customDialog.button("Field: " + fieldComponent.getName()).addListener(new ClickListener(){
      @Override
      public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
        log.log(Level.INFO, "Field: " + fieldComponent.getName());
        customDialog.cancel();
        return true;
      }
    });
  }
}
