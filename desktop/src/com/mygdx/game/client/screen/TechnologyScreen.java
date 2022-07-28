package com.mygdx.game.client.screen;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.game.assets.GameScreenAssetPaths;
import com.mygdx.game.assets.MenuScreenAssets;
import com.mygdx.game.client.GdxGame;
import com.mygdx.game.client.ecs.component.TextureComp;
import com.mygdx.game.client.input.TechnologyTreeInputAdapter;
import com.mygdx.game.client.util.UIElementsCreator;
import com.mygdx.game.client_core.ecs.component.Name;
import com.mygdx.game.client_core.ecs.component.Position;
import com.mygdx.game.client_core.model.Technologies;
import com.mygdx.game.core.ecs.component.Technology;
import lombok.NonNull;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
@Log
public class TechnologyScreen extends ScreenAdapter {

  private final World world;
  private final GdxGame game;
  private final Stage stage;
  private final MenuScreenAssets assets;
  private final List<Integer> technologies;
  private final UIElementsCreator uiElementsCreator;

  private List<Image> technologyImages;

  private final ComponentMapper<Position> positionMapper;
  private final ComponentMapper<Technology> technologyMapper;
  private final ComponentMapper<TextureComp> textureMapper;
  private final ComponentMapper<Name> nameMapper;

  @Inject
  public TechnologyScreen(
          @NonNull GdxGame game,
          @NonNull World world,
          @NonNull Stage stage,
          @NonNull MenuScreenAssets assets,
          @NonNull Technologies technologies,
          @NonNull UIElementsCreator uiElementsCreator
          ) {
    this.game = game;
    this.world = world;
    this.stage = stage;
    this.assets = assets;

    this.positionMapper = world.getMapper(Position.class);
    this.technologyMapper = world.getMapper(Technology.class);
    this.textureMapper = world.getMapper(TextureComp.class);
    this.nameMapper = world.getMapper(Name.class);

    this.technologies = technologies.getAllTechnologies();
    this.uiElementsCreator = uiElementsCreator;

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
    var technologyInputAdapter = new TechnologyTreeInputAdapter(game);
    var inputMultiplexer = new InputMultiplexer(technologyInputAdapter, stage);
    Gdx.input.setInputProcessor(inputMultiplexer);
  }

  private void setUpTechnologyButtons() {
    this.technologyImages = new ArrayList<>();
    for (int entityId : this.technologies) {
      var position = positionMapper.get(entityId).getPosition();
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
