package com.mygdx.game.client.screen;

import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.annotations.AspectDescriptor;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.assets.MenuScreenAssets;
import com.mygdx.game.client.di.StageModule;
import com.mygdx.game.client.ecs.component.Visible;
import com.mygdx.game.client.hud.FieldScreenHUD;
import com.mygdx.game.client.input.ClickInputAdapter;
import com.mygdx.game.client.input.FieldUIInputAdapter;
import com.mygdx.game.client.model.ChosenEntity;
import com.mygdx.game.client.model.InField;
import com.mygdx.game.client.ui.decorations.StarBackgroundGame;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.network.service.ShowSubfieldService;
import com.mygdx.game.core.ecs.component.Building;
import com.mygdx.game.core.ecs.component.SubField;
import com.mygdx.game.core.ecs.component.UnderConstruction;
import com.mygdx.game.core.util.CompositeUpdatable;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Named;

@Log
@GameInstanceScope
public class FieldScreen extends ScreenAdapter {

  private final CompositeUpdatable compositeUpdatable = new CompositeUpdatable();

  private final World world;
  private final InField inField;
  private final Viewport viewport;

  private final Stage stage;
  private final ClickInputAdapter clickInputAdapter;
  private final FieldScreenHUD fieldScreenHUD;
  private final ChosenEntity chosenEntity;
  private final ShowSubfieldService showSubfieldService;
  private final FieldUIInputAdapter subFieldUiInputProcessor;
  private final StarBackgroundGame starBackground;
  private final MenuScreenAssets assets;

  private int fieldParent = -1;

  @AspectDescriptor(one = {SubField.class, Building.class, UnderConstruction.class})
  private EntitySubscription subscription;
  private ComponentMapper<Visible> visibleComponentMapper;
  private Vector3 pos;

  @Inject
  public FieldScreen(
      World world,
      Viewport viewport,
      @Named(StageModule.FIELD_SCREEN) Stage stage,
      ClickInputAdapter clickInputAdapter,
      FieldScreenHUD fieldScreenHUD,
      ChosenEntity chosenEntity,
      ShowSubfieldService showSubfieldService,
      FieldUIInputAdapter subFieldUiInputProcessor,
      InField inField,
      MenuScreenAssets assets
  ) {
    this.world = world;
    this.inField = inField;
    world.inject(this);
    this.viewport = viewport;
    this.stage = stage;
    this.clickInputAdapter = clickInputAdapter;
    this.fieldScreenHUD = fieldScreenHUD;
    this.chosenEntity = chosenEntity;
    this.showSubfieldService = showSubfieldService;
    this.subFieldUiInputProcessor = subFieldUiInputProcessor;
    this.assets = assets;
    this.starBackground = new StarBackgroundGame(assets, stage.getCamera());
  }

  @Override
  public void show() {
    log.info("SubArea shown");
    fieldParent = chosenEntity.pop();
    subscription.addSubscriptionListener(new EntitySubscription.SubscriptionListener() {
      @Override
      public void inserted(IntBag entities) {
        for (int i = 0; i < entities.size(); i++) {
          var entity = entities.get(i);
          visibleComponentMapper.set(entity, true);
        }
      }

      @Override
      public void removed(IntBag entities) {
        for (int i = 0; i < entities.size(); i++) {
          var entity = entities.get(i);
          visibleComponentMapper.set(entity, false);
        }
      }
    });
    inField.setInField(true);
    inField.setField(fieldParent);
    showSubfieldService.flipSubscriptionState(fieldParent);
    saveCameraPosition(viewport.getCamera());
    positionCamera(viewport.getCamera());
    setUpInput();
    fieldScreenHUD.prepareHudSceleton();
  }

  @Override
  public void render(float delta) {
    compositeUpdatable.update(delta);

    starBackground.update(delta);
    stage.getBatch().begin();
    starBackground.draw(stage.getBatch());
    stage.getBatch().setShader(null);
    stage.getBatch().end();

    world.setDelta(delta);
    world.process();
    viewport.getCamera().update();



    stage.draw();
    fieldScreenHUD.draw();

    stage.act(delta);
    fieldScreenHUD.act(delta);
  }

  @Override
  public void resize(int width, int height) {
    viewport.update(width, height);
    starBackground.resize(width, height);
    stage.getViewport().update(width, height, true);
    fieldScreenHUD.resize();
  }

  @Override
  public void hide() {
    restoreCameraPosition(viewport.getCamera());
    showSubfieldService.flipSubscriptionState(fieldParent);
    fieldParent = -1;
    inField.setInField(false);
    inField.setField(-1);
    disposeInput();
  }

  private void positionCamera(@NonNull Camera camera) {
    camera.position.set(-400, 600, -400);
    camera.lookAt(-400, 0, -400);
  }

  private void restoreCameraPosition(Camera camera) {
    camera.position.set(pos);
  }

  private void saveCameraPosition(Camera camera) {
    pos = new Vector3(camera.position);
  }

  private void setUpInput() {
    var inputMultiplexer = new InputMultiplexer(subFieldUiInputProcessor, stage, clickInputAdapter);
    Gdx.input.setInputProcessor(inputMultiplexer);
  }

  private void disposeInput() {
    Gdx.input.setInputProcessor(null);
  }
}
