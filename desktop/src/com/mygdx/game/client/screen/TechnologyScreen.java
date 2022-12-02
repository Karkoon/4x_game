package com.mygdx.game.client.screen;

import com.artemis.ComponentMapper;
import com.artemis.EntitySubscription;
import com.artemis.World;
import com.artemis.annotations.AspectDescriptor;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.mygdx.game.assets.GameConfigAssets;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.ecs.component.TextureComp;
import com.mygdx.game.client.input.TechnologyScreenUiInputAdapter;
import com.mygdx.game.client.ui.CanNotResearchTechnologyDialogFactory;
import com.mygdx.game.client.util.UiElementsCreator;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.ecs.component.Position;
import com.mygdx.game.client_core.model.Technologies;
import com.mygdx.game.client_core.network.service.ResearchTechnologyService;
import com.mygdx.game.config.TechnologyConfig;
import com.mygdx.game.core.ecs.component.EntityConfigId;
import com.mygdx.game.core.ecs.component.InResearch;
import com.mygdx.game.core.ecs.component.Researched;
import com.mygdx.game.core.ecs.component.Technology;
import com.mygdx.game.core.model.TechnologyImpactValue;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import java.util.ArrayList;

@Log
@GameInstanceScope
public class TechnologyScreen extends ScreenAdapter {

  private final Stage stage;
  private final ShapeRenderer shapeRenderer;
  private final Viewport viewport;
  private final GameConfigAssets gameConfigAssets;
  private final @NonNull Technologies technologies;
  private final UiElementsCreator uiElementsCreator;
  private final TechnologyScreenUiInputAdapter technologyScreenUiInputAdapter;
  private final ResearchTechnologyService researchTechnologyService;
  private final World world;
  private final CanNotResearchTechnologyDialogFactory canNotResearchTechnologyDialogFactory;
  private Vector3 pos;
  private final Texture inResearchTexture;
  private final Texture researchedTexture;
  private final Texture unblockedTexture;

  private ComponentMapper<EntityConfigId> entityConfigIdMapper;
  private ComponentMapper<InResearch> inResearchMapper;
  private ComponentMapper<Position> positionMapper;
  private ComponentMapper<Researched> researchedMapper;
  private ComponentMapper<TextureComp> textureMapper;

  boolean draw = false;

  @AspectDescriptor(all = {InResearch.class})
  private EntitySubscription inResearchSubscriber;

  @AspectDescriptor(all = {Technology.class})
  private EntitySubscription technologySubscriber;

  @Inject
  public TechnologyScreen(
      World world,
      Stage stage,
      Viewport viewport,
      GameConfigAssets gameConfigAssets,
      GameScreenAssets gameScreenAssets,
      Technologies technologies,
      UiElementsCreator uiElementsCreator,
      TechnologyScreenUiInputAdapter technologyScreenUiInputAdapter,
      ResearchTechnologyService researchTechnologyService,
      CanNotResearchTechnologyDialogFactory canNotResearchTechnologyDialogFactory
  ) {
    world.inject(this);
    this.world = world;
    this.stage = stage;
    this.shapeRenderer = new ShapeRenderer();
    this.viewport = viewport;
    this.gameConfigAssets = gameConfigAssets;
    this.technologies = technologies;
    this.uiElementsCreator = uiElementsCreator;
    this.technologyScreenUiInputAdapter = technologyScreenUiInputAdapter;
    this.researchTechnologyService = researchTechnologyService;
    this.canNotResearchTechnologyDialogFactory = canNotResearchTechnologyDialogFactory;
    this.inResearchTexture = gameScreenAssets.getTexture( "technologies/inresearch.png");
    this.unblockedTexture = gameScreenAssets.getTexture( "technologies/unblocked.png");
    this.researchedTexture = gameScreenAssets.getTexture( "technologies/researched.png");
  }

  @Override
  public void show() {
    draw = true;
    log.info("Technology tree shown");
    setUpTechnologyButtons();
    setUpInput();
    saveCameraPosition(viewport.getCamera());
    positionCamera(viewport.getCamera());
  }

  @Override
  public void render(float delta) {
    if (draw) {
      for (int i = 0; i < technologies.getAllTechnologies().size(); i++) {
        drawDependencies(technologies.getAllTechnologies().get(i));
      }
    }
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
    draw = false;
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
      var entityConfigId = entityConfigIdMapper.get(entityId);
      var technology = gameConfigAssets.getGameConfigs().get(TechnologyConfig.class, (int) entityConfigId.getId());


      var image = uiElementsCreator.createImage(texture, (int) position.x, (int) position.z);
      var description = createTechDescription(technology, position);
      uiElementsCreator.addHoverPopupWithActor(image, description, stage);

      stage.addActor(image);
      var secondImage = uiElementsCreator.createImage(unblockedTexture, (int) position.x, (int) position.z);
      if (inResearchMapper.has(entityId)) {
        secondImage = uiElementsCreator.createImage(inResearchTexture, (int) position.x, (int) position.z);
      } else if (researchedMapper.has(entityId)) {
        secondImage = uiElementsCreator.createImage(researchedTexture, (int) position.x, (int) position.z);
      } else {
        secondImage = uiElementsCreator.createImage(unblockedTexture, (int) position.x, (int) position.z);
      }
      secondImage.addListener(new ClickListener() {
        @Override
        public void clicked(InputEvent event, float x, float y) {
          researchTechnology(entityId);
        }
      });

      uiElementsCreator.addHoverPopupWithActor(secondImage, description, stage);
      stage.addActor(secondImage);
    }
  }

  private VerticalGroup createTechDescription(TechnologyConfig technology, Vector3 position) {
    var techContainer = uiElementsCreator.createVerticalContainer((int) position.x + 250, (int) position.z + 250, 250, 400);

    var nameLabel = uiElementsCreator.createLabel(technology.getName(), 0, 0);
    uiElementsCreator.scaleLabel(nameLabel, 2.5f);
    techContainer.addActor(nameLabel);

    var requiredScienceLabel = uiElementsCreator.createLabel("Required science: " + technology.getRequiredScience(), 0, 0);
    uiElementsCreator.scaleLabel(requiredScienceLabel, 2.5f);
    techContainer.addActor(requiredScienceLabel);

    var techTypeLabel = uiElementsCreator.createLabel("Type: " + technology.getImpact().getTechnologyImpactType().name, 0, 0);
    uiElementsCreator.scaleLabel(techTypeLabel, 2.5f);
    techContainer.addActor(techTypeLabel);

    for (TechnologyImpactValue impactValue : technology.getImpact().getTechnologyImpactValues()) {
      int value = impactValue.getValue();
      var operation = impactValue.getOperation().name;
      String impactName = impactValue.getParameter().name;
      var impactLabel = uiElementsCreator.createLabel(impactName + " " + operation + " " + value, 0, 0);
      uiElementsCreator.scaleLabel(impactLabel, 2.5f);
      techContainer.addActor(impactLabel);
    }
    return techContainer;
  }

  public void researchTechnology(int entityId) {
    if (inResearchMapper.has(entityId)) {
      var dialog = canNotResearchTechnologyDialogFactory.createAndShow("Can't research new technology - in research state");
      log.info("Can't research");
      stage.addActor(dialog);
    } else if (inResearchSubscriber.getEntities().size() > 0) {
      var dialog = canNotResearchTechnologyDialogFactory.createAndShow("Can't research new technology - researching another");
      log.info("Can't research");
      stage.addActor(dialog);
    } else if (researchedMapper.has(entityId)) {
      var dialog = canNotResearchTechnologyDialogFactory.createAndShow("Can't research new technology - it's researched");
      log.info("Can't research");
      stage.addActor(dialog);
    } else if (!allRequiredTechnologiesResearched(entityId)) {
      var dialog = canNotResearchTechnologyDialogFactory.createAndShow("Can't research new technology - dependent technologies not researched");
      log.info("Can't research");
      stage.addActor(dialog);
    } else {
      researchTechnologyService.researchTechnology(entityId);
    }
  }

  private boolean allRequiredTechnologiesResearched(int techEntityId) {
    var entityConfigId = entityConfigIdMapper.get(techEntityId);
    var technologyConfig = gameConfigAssets.getGameConfigs().get(TechnologyConfig.class, entityConfigId.getId());
    var dependencies = new ArrayList<>(technologyConfig.getDependencies());
    for (int i = 0; i < technologies.getAllTechnologies().size(); i++) {
      int alltechEntityId = technologies.getAllTechnologies().get(i);
      if (researchedMapper.has(alltechEntityId)) {
        if (dependencies.contains((int) entityConfigIdMapper.get(alltechEntityId).getId())) {
          Integer integer = (int) entityConfigIdMapper.get(alltechEntityId).getId();
          dependencies.remove(integer);
        }
      }
    }
    return dependencies.size() == 0;
  }

  private void drawDependencies(int entityId) {
    var entityConfigId = entityConfigIdMapper.get(entityId);
    var position = positionMapper.get(entityId).getValue();
    var dependencies = gameConfigAssets.getGameConfigs().get(TechnologyConfig.class, entityConfigId.getId()).getDependencies();
    for (Integer dependency : dependencies) {
      int entityIdOfConfig = findEntityIdOfConfig(dependency);
      var dependencyPosition = positionMapper.get(entityIdOfConfig).getValue();
      shapeRenderer.setProjectionMatrix(stage.getBatch().getProjectionMatrix());
      shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
      if (researchedMapper.has(entityIdOfConfig))
        shapeRenderer.setColor(0, 1, 0, 1);
      else if (inResearchMapper.has(entityIdOfConfig))
        shapeRenderer.setColor(1, 1, 0, 1);
      else
        shapeRenderer.setColor(1, 0, 0, 1);
      shapeRenderer.rectLine(position.x, position.z, dependencyPosition.x, dependencyPosition.z, 10f);
      shapeRenderer.end();
    }
  }

  private int findEntityIdOfConfig(int configId) {
    for (int i = 0; i < technologySubscriber.getEntities().size(); i++) {
      int entityId = technologySubscriber.getEntities().get(i);
      if (entityConfigIdMapper.get(entityId).getId() == configId)
        return entityId;
    }
    return -1;
  }
}
