package com.mygdx.game.client.screen;

import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.annotations.AspectDescriptor;
import com.artemis.utils.IntBag;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.ecs.component.TextureComp;
import com.mygdx.game.client.input.TechnologyScreenUiInputAdapter;
import com.mygdx.game.client.util.UiElementsCreator;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.ecs.component.Position;
import com.mygdx.game.client_core.model.Technologies;
import com.mygdx.game.client_core.network.service.ResearchTechnologyService;
import com.mygdx.game.core.ecs.component.Name;
import com.mygdx.game.core.ecs.component.Research;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;

@Log
@GameInstanceScope
public class TechnologyScreen extends ScreenAdapter {

  private final Stage stage;
  private final GameScreenAssets gameScreenAssets;
  private final @NonNull Technologies technologies;
  private final UiElementsCreator uiElementsCreator;
  private final TechnologyScreenUiInputAdapter technologyScreenUiInputAdapter;
  private final ResearchTechnologyService researchTechnologyService;
  private final World world;

  @AspectDescriptor(all = {Research.class})
  private EntitySubscription subscription;

  private ComponentMapper<Position> positionMapper;
  private ComponentMapper<TextureComp> textureMapper;
  private ComponentMapper<Name> nameMapper;


  @Inject
  public TechnologyScreen(
      World world,
      Stage stage,
      GameScreenAssets gameScreenAssets,
      Technologies technologies,
      UiElementsCreator uiElementsCreator,
      TechnologyScreenUiInputAdapter technologyScreenUiInputAdapter,
      ResearchTechnologyService researchTechnologyService
  ) {
    world.inject(this);
    this.world = world;
    this.stage = stage;
    this.gameScreenAssets = gameScreenAssets;
    this.technologies = technologies;
    this.uiElementsCreator = uiElementsCreator;
    this.technologyScreenUiInputAdapter = technologyScreenUiInputAdapter;
    this.researchTechnologyService = researchTechnologyService;

    addListeners();
  }

  @Override
  public void show() {
    log.info("Technology tree shown");
    setUpTechnologyButtons();
    setUpInput();
  }

  @Override
  public void render(float delta) {
    stage.draw();
    stage.act(delta);
  }

  @Override
  public void resize(int width, int height) {
    stage.getViewport().update(width, height);
    super.resize(width, height);
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

      image.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
          researchTechnologyService.researchTechnology(entityId);
          world.process();
        }
      });

      stage.addActor(image);
    }

    for (int i = 0; i < subscription.getEntities().size(); i++) {
      addFlagToResearchedTechnology(subscription.getEntities().get(i));
    }
  }

  private void addFlagToResearchedTechnology(int entityId) {
    var position = positionMapper.get(entityId).getValue();
    var texture = gameScreenAssets.getTexture( "technologies/researched.png");
    var name = nameMapper.get(entityId);

    var image = uiElementsCreator.createImage(texture, (int) position.x, (int) position.z);
    var label = uiElementsCreator.createLabel(name.getName(), (int) position.x + 200, (int) position.z + 200);
    uiElementsCreator.scaleLabel(label, 2.5f);
    uiElementsCreator.addHoverPopupWithActor(image, label, stage);

    stage.addActor(image);
  }

  private void addListeners() {
    subscription.addSubscriptionListener(new EntitySubscription.SubscriptionListener() {
      @Override
      public void inserted(IntBag entities) {
        for (int i = 0; i < entities.size(); i++) {
          addFlagToResearchedTechnology(entities.get(i));
        }
      }

      @Override
      public void removed(IntBag entities) {
      }
    });
  }

}
