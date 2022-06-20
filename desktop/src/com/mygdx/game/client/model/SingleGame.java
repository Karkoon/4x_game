package com.mygdx.game.client.model;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.github.czyzby.websocket.WebSocketHandler;
import com.mygdx.game.assets.GameScreenAssets;
import com.mygdx.game.client.di.scope.SingleGameScope;
import com.mygdx.game.client.ecs.component.Position;
import com.mygdx.game.client.ecs.entityfactory.FieldFactory;
import com.mygdx.game.client.ecs.entityfactory.UnitFactory;
import com.mygdx.game.client.network.ComponentMessageListener;
import com.mygdx.game.client.network.GameStartService;
import com.mygdx.game.config.FieldConfig;
import com.mygdx.game.config.GameConfigs;
import com.mygdx.game.config.UnitConfig;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.EntityConfigId;
import com.mygdx.game.core.network.messages.GameStartedMessage;
import lombok.Getter;
import lombok.extern.java.Log;

import javax.inject.Inject;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;
import static com.github.czyzby.websocket.WebSocketListener.NOT_HANDLED;

@Getter
@SingleGameScope
@Log
public class SingleGame {

  private final World world;
  private final GameState state;
  private final PlayerScore score;
  private final Lifecycle lifecycle;
  private final GameStartService gameStartService;
  private final ComponentMessageListener listener;
  private final GameScreenAssets assets;
  private final FieldFactory fieldFactory;
  private final UnitFactory unitFactory;
  private final WebSocketHandler webSocketHandler;
  private final ComponentMapper<Position> positionMapper;
  private final ComponentMapper<Coordinates> coordinatesMapper;

  @Inject
  public SingleGame(
      World world,
      GameState state,
      PlayerScore score,
      Lifecycle lifecycle,
      GameStartService gameStartService,
      ComponentMessageListener listener,
      GameScreenAssets assets,
      FieldFactory fieldFactory,
      UnitFactory unitFactory,
      WebSocketHandler webSocketHandler
  ) {
    this.webSocketHandler = webSocketHandler;
    log.info("new Single Game!!");
    this.world = world;
    this.state = state;
    this.score = score;
    this.lifecycle = lifecycle;
    this.gameStartService = gameStartService;
    this.listener = listener;
    this.assets = assets;
    this.fieldFactory = fieldFactory;
    this.unitFactory = unitFactory;

    positionMapper = world.getMapper(Position.class);
    coordinatesMapper = world.getMapper(Coordinates.class);
  }

  public void registerHandlers() {
    webSocketHandler.registerHandler(GameStartedMessage.class, ((webSocket, o) -> {
      lifecycle.changeState(Lifecycle.State.IN_PROGRESS);
      return FULLY_HANDLED;
    }));

    listener.registerHandler(EntityConfigId.class, (webSocket, worldEntity, packet) -> { // todo zmienić znowu na serwisy i fabryki,
      // todo bo to jednak nie jest komponent tylko pojedyncza wiadomość xD
      // albo coś
      var entityConfigId = ((EntityConfigId) packet).getId();
      if (entityConfigId >= 1 && entityConfigId <= GameConfigs.FIELD_AMOUNT) { // 1, 2 są z plików jsona EntityConfigów
        log.info("field id " + worldEntity);
        var config = assets.getGameConfigs().get(FieldConfig.class, entityConfigId);
        fieldFactory.createEntity(config, worldEntity);
        return FULLY_HANDLED;
      } else if (entityConfigId > GameConfigs.FIELD_AMOUNT && entityConfigId <= GameConfigs.UNIT_AMOUNT + GameConfigs.FIELD_AMOUNT) {
        log.info("unit id " + worldEntity);
        var config = assets.getGameConfigs().get(UnitConfig.class, entityConfigId);
        unitFactory.createEntity(config, worldEntity);
        return FULLY_HANDLED;
      }
      return NOT_HANDLED;
    });

    listener.registerHandler(Position.class, (webSocket, worldEntity, component) -> { // Position should be changed to CoordinatesComponent
      log.info("Read position component " + worldEntity);
      var newPosition = (Position) component;
      var entityPosition = positionMapper.create(worldEntity);
      entityPosition.setPosition(newPosition.getPosition());
      return FULLY_HANDLED;
    });


    listener.registerHandler(Coordinates.class, (webSocket, worldEntity, component) -> {
      log.info("Read coordinates component " + worldEntity);
      var newCoordinates = (Coordinates) component;
      var entityCoordinates = coordinatesMapper.create(worldEntity);
      state.removeEntity(worldEntity);
      entityCoordinates.setCoordinates(newCoordinates);
      state.saveEntity(worldEntity);
      return FULLY_HANDLED;
    });
  }

  public void process(float delta) {
    world.setDelta(delta);
    world.process();
  }
}
