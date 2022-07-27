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
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.mygdx.game.assets.GameScreenAssetPaths;
import com.mygdx.game.assets.MenuScreenAssets;
import com.mygdx.game.client.GdxGame;
import com.mygdx.game.client.ecs.component.TextureComp;
import com.mygdx.game.client.input.TechnologyTreeInputAdapter;
import com.mygdx.game.client_core.ecs.component.Name;
import com.mygdx.game.client_core.ecs.component.Position;
import com.mygdx.game.client_core.model.GameState;
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
  private final GameState gameState;
  private final List<Integer> technologies;

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
          @NonNull GameState gameState
          ) {
    this.game = game;
    this.world = world;
    this.stage = stage;
    this.assets = assets;
    this.gameState = gameState;

    this.positionMapper = world.getMapper(Position.class);
    this.technologyMapper = world.getMapper(Technology.class);
    this.textureMapper = world.getMapper(TextureComp.class);
    this.nameMapper = world.getMapper(Name.class);

    this.technologies = gameState.getAllTechnologies();
    setUpTechnologyButtons();

    Gdx.input.setInputProcessor(stage);
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

      var image = createAndPositionButton(position, texture);
      createTechnologyDescriptionDisplaying(image, name, position);

      technologyImages.add(image);
      stage.addActor(image);
    }
  }

  private Image createAndPositionButton(Vector3 position, TextureComp texture) {
    var image = new Image(new TextureRegionDrawable(new TextureRegion(texture.getTexture())));
    image.setPosition(position.x, position.z);
    return image;
  }

  private void createTechnologyDescriptionDisplaying(Image image, Name name, Vector3 position) {
    var skin = assets.getSkin(GameScreenAssetPaths.DIALOG_SKIN);
    skin.getFont("default-font").getData().scale(1.5f);

    TextField description = new TextField(name.getName(), skin);
    description.setPosition(position.x + 200, position.z + 200);
    description.setHeight(150);
    description.setWidth(300);

    image.addListener(new ClickListener(){
      @Override
      public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
        stage.addActor(description);
      }
    });

    image.addListener(new ClickListener(){
      @Override
      public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
        description.remove();
      }
    });
  }
}
