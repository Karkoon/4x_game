package com.mygdx.game.client.screen;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.mygdx.game.client.ecs.component.TextureComp;
import com.mygdx.game.client.input.TechnologyScreenUiInputAdapter;
import com.mygdx.game.client.util.UiElementsCreator;
import com.mygdx.game.client_core.ecs.component.Name;
import com.mygdx.game.client_core.ecs.component.Position;
import com.mygdx.game.client_core.model.Technologies;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
@Log
public class TechnologyScreen extends ScreenAdapter {

  private final Stage stage;
  private final @NonNull Technologies technologies;
  private final UiElementsCreator uiElementsCreator;
  private final TechnologyScreenUiInputAdapter technologyScreenUiInputAdapter;

  private List<Image> technologyImages;

  private final ComponentMapper<Position> positionMapper;
  private final ComponentMapper<TextureComp> textureMapper;
  private final ComponentMapper<Name> nameMapper;

  @Inject
  public TechnologyScreen(
          @NonNull World world,
          @NonNull Stage stage,
          @NonNull Technologies technologies,
          @NonNull UiElementsCreator uiElementsCreator,
          @NonNull TechnologyScreenUiInputAdapter technologyScreenUiInputAdapter
          ) {
    this.stage = stage;

    this.positionMapper = world.getMapper(Position.class);
    this.textureMapper = world.getMapper(TextureComp.class);
    this.nameMapper = world.getMapper(Name.class);

    this.technologies = technologies;
    this.uiElementsCreator = uiElementsCreator;
    this.technologyScreenUiInputAdapter = technologyScreenUiInputAdapter;

    setUpTechnologyButtons();
  }

  @Override
  public void show() {
    log.info("Technology tree shown");

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
    this.technologyImages = new ArrayList<>();
    for (int i = 0; i < allTechnologies.size; i++) {
      int entityId = allTechnologies.get(i);
      var position = positionMapper.get(entityId).getValue();
      var texture = textureMapper.get(entityId);
      var name = nameMapper.get(entityId);

      var image = uiElementsCreator.createImage(position, texture);
      var textField = uiElementsCreator.createTextField(position, name.getName());
      uiElementsCreator.addHoverPopupWithActor(image, textField, stage);

      technologyImages.add(image);
      stage.addActor(image);
    }
  }

}
