package com.mygdx.game.bot.screen;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.ScreenAdapter;
import com.mygdx.game.bot.GdxGame;
import com.mygdx.game.bot.hud.NextFieldUtil;
import com.mygdx.game.bot.hud.NextUnitUtil;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceNetworkModule;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.model.ActiveToken;
import com.mygdx.game.client_core.model.ChangesApplied;
import com.mygdx.game.client_core.model.PlayerInfo;
import com.mygdx.game.client_core.model.PredictedIncome;
import com.mygdx.game.client_core.network.QueueMessageListener;
import com.mygdx.game.client_core.network.service.EndTurnService;
import com.mygdx.game.client_core.network.service.MoveEntityService;
import com.mygdx.game.client_core.network.service.ResearchTechnologyService;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.network.messages.ChangeTurnMessage;
import com.mygdx.game.core.network.messages.GameInterruptedMessage;
import com.mygdx.game.core.network.messages.WinAnnouncementMessage;
import dagger.Lazy;
import lombok.extern.java.Log;

import javax.inject.Inject;
import javax.inject.Named;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@Log
@GameInstanceScope
public class GameScreen extends ScreenAdapter {

  private final World world;

  private final PredictedIncome predictedIncome;
  private final NextUnitUtil nextUnitUtil;
  private final PlayerInfo playerInfo;
  private final ActiveToken activeToken;
  private final GdxGame game;
  private final Lazy<FieldScreen> fieldScreen;
  private final ResearchTechnologyService technologyScreen;
  private final QueueMessageListener queueMessageListener;
  private final MoveEntityService moveEntityService;
  private final EndTurnService endTurnService;
  private final NextFieldUtil nextFieldUtil;
  private final ChangesApplied changesApplied;

  private boolean initialized = false;

  private ComponentMapper<Coordinates> coordinatesComponentMapper;

  @Inject
  public GameScreen(
      World world,
      PredictedIncome predictedIncome,
      NextUnitUtil nextUnitUtil,
      PlayerInfo playerInfo,
      ActiveToken activeToken,
      GdxGame game,
      Lazy<FieldScreen> fieldScreen,
      ResearchTechnologyService technologyScreen,
      @Named(GameInstanceNetworkModule.GAME_INSTANCE) QueueMessageListener queueMessageListener,
      MoveEntityService moveEntityService,
      EndTurnService endTurnService,
      NextFieldUtil nextFieldUtil,
      ChangesApplied changesApplied
  ) {
    this.world = world;
    this.predictedIncome = predictedIncome;
    this.nextUnitUtil = nextUnitUtil;
    this.playerInfo = playerInfo;
    this.activeToken = activeToken;
    this.game = game;
    this.fieldScreen = fieldScreen;
    this.technologyScreen = technologyScreen;
    this.queueMessageListener = queueMessageListener;
    this.moveEntityService = moveEntityService;
    this.endTurnService = endTurnService;
    this.nextFieldUtil = nextFieldUtil;
    this.changesApplied = changesApplied;
    this.world.inject(this);
  }

  @Override
  public void show() {
    log.info("GameScreen shown");
    if (!initialized) {
      queueMessageListener.registerHandler(WinAnnouncementMessage.class, handleGameWon());
      queueMessageListener.registerHandler(GameInterruptedMessage.class, handleGameInterrupted());

      queueMessageListener.registerHandler(ChangeTurnMessage.class, ((webSocket, message) -> {
        runBotIteration();
        return FULLY_HANDLED;
      }));

      initialized = true;
      changesApplied.setChangesAppliedListener(this::runBotIteration);
    }
  }

  private QueueMessageListener.Handler<WinAnnouncementMessage> handleGameWon() {
    return (webSocket, message) -> {
      log.info("game finished");
      exit();
      return true;
    };
  }
  private QueueMessageListener.Handler<GameInterruptedMessage> handleGameInterrupted() {
    return (webSocket, message) -> {
      log.info("game interrupted");
      exit();
      return true;
    };
  }

  private void runBotIteration() {
    log.info("run bot iteration");
    if (!activeToken.isActiveToken(playerInfo.getToken())) {
      log.info("quick exit");
      return;
    }
    log.info("start turn");
    var unit = nextUnitUtil.selectNextUnit();
    if (unit == 0xC0FFEE) { // todo ensure the case where a unit that has moveRange and attack but no possible
      // ways to use it be skipped
      log.info("end turn");
      endTurnService.endTurn();
      return;
    }
    var field = nextFieldUtil.selectFieldInRangeOfUnit(unit);
    log.info("moving entity");
    moveEntityService.moveEntity(unit, coordinatesComponentMapper.get(field));
  }

  @Override
  public void render(float delta) {
    world.setDelta(delta);
    world.process();
  }

  public void exit() {
    dispose();
    game.changeToGameRoomScreen();
  }

  public void setActivePlayerToken(String playerToken) {
    activeToken.setActiveToken(playerToken);
    runBotIteration();
  }


}
