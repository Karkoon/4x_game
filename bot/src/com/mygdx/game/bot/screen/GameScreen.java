package com.mygdx.game.bot.screen;

import com.artemis.ComponentMapper;
import com.artemis.World;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.mygdx.game.bot.GdxGame;
import com.mygdx.game.bot.hud.UnitUtil;
import com.mygdx.game.bot.util.BotAttackUtil;
import com.mygdx.game.bot.util.BotMoveUtil;
import com.mygdx.game.bot.util.BotTechnologyUtil;
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
import com.mygdx.game.core.ecs.component.CanAttack;
import com.mygdx.game.core.ecs.component.Coordinates;
import com.mygdx.game.core.ecs.component.Stats;
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

  private final BotAttackUtil botAttackUtil;
  private final BotMoveUtil botMoveUtil;
  private final BotTechnologyUtil botTechnologyUtil;
  private final PredictedIncome predictedIncome;
  private final UnitUtil unitUtil;
  private final PlayerInfo playerInfo;
  private final ActiveToken activeToken;
  private final GdxGame game;
  private final Lazy<FieldScreen> fieldScreen;
  private final ResearchTechnologyService technologyScreen;
  private final QueueMessageListener queueMessageListener;
  private final MoveEntityService moveEntityService;
  private final EndTurnService endTurnService;
  private final ChangesApplied changesApplied;

  private boolean initialized = false;

  private ComponentMapper<Coordinates> coordinatesComponentMapper;
  private ComponentMapper<Stats> statsMapper;
  private ComponentMapper<CanAttack> canAttackComponentMapper;
  private boolean lock = false;

  @Inject
  public GameScreen(
      World world,
      BotAttackUtil botAttackUtil,
      BotMoveUtil botMoveUtil,
      BotTechnologyUtil botTechnologyUtil,
      PredictedIncome predictedIncome,
      UnitUtil unitUtil,
      PlayerInfo playerInfo,
      ActiveToken activeToken,
      GdxGame game,
      Lazy<FieldScreen> fieldScreen,
      ResearchTechnologyService technologyScreen,
      @Named(GameInstanceNetworkModule.GAME_INSTANCE) QueueMessageListener queueMessageListener,
      MoveEntityService moveEntityService,
      EndTurnService endTurnService,
      ChangesApplied changesApplied
  ) {
    this.world = world;
    this.botAttackUtil = botAttackUtil;
    this.botMoveUtil = botMoveUtil;
    this.botTechnologyUtil = botTechnologyUtil;
    this.predictedIncome = predictedIncome;
    this.unitUtil = unitUtil;
    this.playerInfo = playerInfo;
    this.activeToken = activeToken;
    this.game = game;
    this.fieldScreen = fieldScreen;
    this.technologyScreen = technologyScreen;
    this.queueMessageListener = queueMessageListener;
    this.moveEntityService = moveEntityService;
    this.endTurnService = endTurnService;
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
      log.info(Thread.currentThread().getName()+ " " + Thread.currentThread().getId() + "game interrupted");
      exit();
      return true;
    };
  }

  boolean a = false;
  private void runBotIteration() {
    log.info(Thread.currentThread().getName()+ " " + Thread.currentThread().getId() + "run bot iteration");
    if (a) {
      log.info("wtf");
      Gdx.app.exit();
    }
    a = true;
    if (!activeToken.isActiveToken(playerInfo.getToken())) {
      a = false;
      log.info("quick exit");
      return;
    }
    endTurn();
    log.info("ending bot iteration");
    return;
/*    log.info("start turn");
    if (selectedUnit == 0xC0FFEE) {
      log.info("selecting next unit");
      selectedUnit = unitUtil.selectNextUnit(); // obtain next unit to control
      // todo ensure the case where a unit that has moveRange and attack but no possible
      // ways to use it be skipped
    } else {
      log.info("continuing unit " + selectedUnit);
    }
    if (selectedUnit == 0xC0FFEE) { // if that unit doesn't exist then end turn
      endTurn();
      return;
    }
    switch (currentStage) { // the bot has several states in which it can exist
      case PRE_MOVE_ATTACK -> handlePreMoveAttackStage();
      case MOVE -> handleMoveStage();
      case POST_MOVE_ATTACK -> handlePostMoveAttackStage();
    }*/
  }

  private void handlePostMoveAttackStage() {
    log.info("post move attack stage");
    if (canAttackComponentMapper.get(selectedUnit) != null
        && canAttackComponentMapper.get(selectedUnit).isCanAttack()) {
      botAttackUtil.attack(selectedUnit);
    }
    currentStage = BotStage.PRE_MOVE_ATTACK;
    selectedUnit = 0xC0FFEE;
    runBotIteration();
  }

  private void handlePreMoveAttackStage() {
    log.info("pre move attack stage");
    if (!botAttackUtil.attack(selectedUnit)) {
      handleMoveStage();
    }
    currentStage = BotStage.MOVE;
  }

  private void handleMoveStage() {
    log.info("move stage");
    botMoveUtil.move(selectedUnit);
    currentStage = BotStage.POST_MOVE_ATTACK;
  }

  private void endTurn() {
    a = false;
    log.info("end turn");
    botTechnologyUtil.research();
    endTurnService.endTurn();
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

  private int selectedUnit = 0xC0FFEE;
  private BotStage currentStage = BotStage.PRE_MOVE_ATTACK;

  enum BotStage {
    PRE_MOVE_ATTACK, MOVE, POST_MOVE_ATTACK
  }

}
