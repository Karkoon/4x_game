package com.mygdx.game.client.screen;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.client.ModelInstanceRenderer;
import com.mygdx.game.client.di.StageModule;
import com.mygdx.game.client.ecs.component.Visible;
import com.mygdx.game.client.input.CameraMoverInputProcessor;
import com.mygdx.game.client.input.SubFieldUiInputProcessor;
import com.mygdx.game.client.model.ChosenEntity;
import com.mygdx.game.client.model.InField;
import com.mygdx.game.client_core.network.ShowSubfieldService;
import com.mygdx.game.core.ecs.component.Field;
import com.mygdx.game.core.util.CompositeUpdatable;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
@Log
public class FieldScreen extends ScreenAdapter {

  private final CompositeUpdatable compositeUpdatable = new CompositeUpdatable();

  private final World world;
  private final InField inField;
  private final Viewport viewport;

  private final Stage stage;
  private final ChosenEntity chosenEntity;
  private final ShowSubfieldService showSubfieldService;
  private final SubFieldUiInputProcessor subFieldUiInputProcessor;
  private ComponentMapper<Visible> visibleComponentMapper;
  private ComponentMapper<Field> fieldComponentMapper;

  private int fieldParent = -1;

  @Inject
  public FieldScreen(
      @NonNull World world,
      @NonNull Viewport viewport,
      @NonNull @Named(StageModule.GAME_SCREEN) Stage stage,
      @NonNull ChosenEntity chosenEntity,
      @NonNull ShowSubfieldService showSubfieldService,
      @NonNull SubFieldUiInputProcessor subFieldUiInputProcessor,
      @NonNull InField inField
  ) {
    this.world = world;
    this.inField = inField;
    world.inject(this);
    this.viewport = viewport;
    this.stage = stage;
    this.chosenEntity = chosenEntity;
    this.showSubfieldService = showSubfieldService;
    this.subFieldUiInputProcessor = subFieldUiInputProcessor;
  }

  @Override
  public void show() {
    log.info("SubArea shown");
    inField.setInField(true);
    fieldParent = chosenEntity.pop();
    setSubfieldsVisibility(fieldParent, true);
    showSubfieldService.flipSubscriptionState(fieldParent);
    positionCamera(viewport.getCamera());
    setUpInput();
  }

  private void setSubfieldsVisibility(int fieldParent, boolean visibility) {
    var subfields = fieldComponentMapper.get(fieldParent).getSubFields();
    for (int i = 0; i < subfields.size; i++) {
      visibleComponentMapper.set(subfields.get(i), visibility);
    }
  }

  @Override
  public void render(float delta) {
    compositeUpdatable.update(delta);
    world.setDelta(delta);
    world.process();
    viewport.getCamera().update();
    stage.draw();
    stage.act(delta);
  }

  @Override
  public void resize(int width, int height) {
    viewport.update(width, height);
    stage.getViewport().update(width, height, true);
  }


  @Override
  public void hide() {
    inField.setInField(false);
    setSubfieldsVisibility(fieldParent, false);
    showSubfieldService.flipSubscriptionState(fieldParent);
    fieldParent = -1;
  }

  private void positionCamera(@NonNull Camera camera) {
    camera.position.set(0, 600, 0);
    camera.lookAt(0, 0, 0);
  }

  private void setUpInput() {
    var cameraInputProcessor = new CameraMoverInputProcessor(viewport);
    var inputMultiplexer = new InputMultiplexer(cameraInputProcessor, subFieldUiInputProcessor, stage);
    compositeUpdatable.addUpdatable(cameraInputProcessor.getCameraControl());
    Gdx.input.setInputProcessor(inputMultiplexer);
  }
}
