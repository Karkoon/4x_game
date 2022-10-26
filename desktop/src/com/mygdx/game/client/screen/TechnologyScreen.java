package com.mygdx.game.client.screen;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.ecs.component.TextureComp;
import com.mygdx.game.client.input.TechnologyScreenUiInputAdapter;
import com.mygdx.game.client.util.UiElementsCreator;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.ecs.component.Position;
import com.mygdx.game.client_core.model.Technologies;
import com.mygdx.game.client_core.network.service.ResearchTechnologyService;
import com.mygdx.game.core.ecs.component.Name;
import com.mygdx.game.core.ecs.component.InResearch;
import com.mygdx.game.core.ecs.component.Researched;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
@GameInstanceScope
public class TechnologyScreen extends ScreenAdapter {

  private final Stage stage;
  private final Viewport viewport;
  private final GameScreenAssets gameScreenAssets;
  private final @NonNull Technologies technologies;
  private final UiElementsCreator uiElementsCreator;
  private final TechnologyScreenUiInputAdapter technologyScreenUiInputAdapter;
  private final ResearchTechnologyService researchTechnologyService;
  private final World world;
  private final Texture inResearchTexture;
  private final Texture researchedTexture;
  private final Texture unblockedTexture;

  private ComponentMapper<InResearch> inResearchMapper;
  private ComponentMapper<Name> nameMapper;
  private ComponentMapper<Position> positionMapper;
  private ComponentMapper<Researched> researchedMapper;
  private ComponentMapper<TextureComp> textureMapper;
  private Vector3 pos;


  @Inject
  public TechnologyScreen(
      World world,
      Stage stage,
      Viewport viewport,
      GameScreenAssets gameScreenAssets,
      Technologies technologies,
      UiElementsCreator uiElementsCreator,
      TechnologyScreenUiInputAdapter technologyScreenUiInputAdapter,
      ResearchTechnologyService researchTechnologyService
  ) {
    world.inject(this);
    this.world = world;
    this.stage = stage;
    this.viewport = viewport;
    this.gameScreenAssets = gameScreenAssets;
    this.technologies = technologies;
    this.uiElementsCreator = uiElementsCreator;
    this.technologyScreenUiInputAdapter = technologyScreenUiInputAdapter;
    this.researchTechnologyService = researchTechnologyService;
    this.inResearchTexture = gameScreenAssets.getTexture( "technologies/inresearch.png");
    this.unblockedTexture = gameScreenAssets.getTexture( "technologies/unblocked.png");
    this.researchedTexture = gameScreenAssets.getTexture( "technologies/researched.png");
  }

  @Override
  public void show() {
    log.info("Technology tree shown");
    setUpTechnologyButtons();
    setUpInput();
    saveCameraPosition(viewport.getCamera());
    positionCamera(viewport.getCamera());
  }

  @Override
  public void render(float delta) {
    world.setDelta(delta);
    world.process();
    stage.draw();
    stage.act(delta);
    viewport.getCamera().update();
  }

  @Override
  public void resize(int width, int height) {
    viewport.update(width, height);
    stage.getViewport().update(width, height, true);
  }

  @Override
  public void hide() {
    restoreCameraPosition(viewport.getCamera());
  }

  public void refresh() {
    stage.clear();
    setUpTechnologyButtons();
  }

  private void positionCamera(@NonNull Camera camera) {
    camera.position.set(-1000, 600, -1000);
    camera.lookAt(-1000, 0, -1000);
  }

  private void restoreCameraPosition(Camera camera) {
    camera.position.set(pos);
  }

  private void saveCameraPosition(Camera camera) {
    pos = new Vector3(camera.position);
  }

  private void setUpInput() {
    var inputMultiplexer = new InputMultiplexer(technologyScreenUiInputAdapter, stage);
    Gdx.input.setInputProcessor(inputMultiplexer);
  }

  private void setUpTechnologyButtons() {
    var allTechnologies = this.technologies.getAllTechnologies();
    for (int i = 0; i < allTechnologies.size(); i++) {
      int entityId = allTechnologies.get(i);
      var position = positionMapper.get(entityId).getValue();
      var texture = textureMapper.get(entityId).getTexture();
      var name = nameMapper.get(entityId);

      var image = uiElementsCreator.createImage(texture, (int) position.x, (int) position.z);
      var label = uiElementsCreator.createLabel(name.getName(), (int) position.x + 250, (int) position.z + 250);
      uiElementsCreator.scaleLabel(label, 2.5f);
      uiElementsCreator.addHoverPopupWithActor(image, label, stage);

      stage.addActor(image);
      var secondImage = uiElementsCreator.createImage(unblockedTexture, (int) position.x, (int) position.z);
      if (inResearchMapper.has(entityId)) {
        secondImage = uiElementsCreator.createImage(inResearchTexture, (int) position.x, (int) position.z);
      } else if (researchedMapper.has(entityId)) {
        secondImage = uiElementsCreator.createImage(inResearchTexture, (int) position.x, (int) position.z);
      } else {
        secondImage.addListener(new ClickListener() {
          @Override
          public void clicked(InputEvent event, float x, float y) {
            researchTechnologyService.researchTechnology(entityId);
            world.process();
          }
        });
      }
      uiElementsCreator.addHoverPopupWithActor(secondImage, label, stage);
      stage.addActor(secondImage);
    }

  }
}
