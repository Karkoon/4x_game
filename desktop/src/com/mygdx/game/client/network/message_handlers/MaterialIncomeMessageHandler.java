package com.mygdx.game.client.network.message_handlers;

import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client.hud.GameScreenHUD;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.model.PredictedIncome;
import com.mygdx.game.client_core.network.QueueMessageListener;
import com.mygdx.game.core.model.MaterialBase;
import com.mygdx.game.core.network.messages.MaterialIncomeMessage;
import lombok.extern.java.Log;

import javax.inject.Inject;

import java.util.Map;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@GameInstanceScope
@Log
public class MaterialIncomeMessageHandler implements QueueMessageListener.Handler<MaterialIncomeMessage>  {

  private final GameScreenHUD gameScreenHUD;
  private final PredictedIncome predictedIncome;

  @Inject
  public MaterialIncomeMessageHandler(
      GameScreenHUD gameScreenHUD,
      PredictedIncome predictedIncome
  ) {
    this.gameScreenHUD = gameScreenHUD;
    this.predictedIncome = predictedIncome;
  }

  @Override
  public boolean handle(WebSocket webSocket, MaterialIncomeMessage message) {
    log.info("Handle material income message ");
    Map<String, Integer> incomes = message.getIncomes();
    for (MaterialBase base : MaterialBase.values()) {
      var value = incomes.get(base.toString());
      predictedIncome.setIncome(base, value);
    }
    gameScreenHUD.prepareHudSceleton();
    return FULLY_HANDLED;
  }

}
