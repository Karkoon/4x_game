package com.mygdx.game.bot.network.message_handlers;

import com.github.czyzby.websocket.WebSocket;
import com.mygdx.game.client_core.di.gameinstance.GameInstanceScope;
import com.mygdx.game.client_core.model.PredictedIncome;
import com.mygdx.game.client_core.network.QueueMessageListener;
import com.mygdx.game.core.model.MaterialBase;
import com.mygdx.game.core.network.messages.MaterialIncomeMessage;
import lombok.extern.java.Log;

import javax.inject.Inject;

import static com.github.czyzby.websocket.WebSocketListener.FULLY_HANDLED;

@Log
@GameInstanceScope
public class MaterialIncomeMessageHandler implements QueueMessageListener.Handler<MaterialIncomeMessage>  {

  private final PredictedIncome predictedIncome;

  @Inject
  public MaterialIncomeMessageHandler(
      PredictedIncome predictedIncome
  ) {
    this.predictedIncome = predictedIncome;
  }

  @Override
  public boolean handle(WebSocket webSocket, MaterialIncomeMessage message) {
    log.info("Handle material income message ");
    var incomes = message.getIncomes();
    for (var base : MaterialBase.values()) {
      var value = incomes.get(base.toString());
      predictedIncome.setIncome(base, value);
    }
    return FULLY_HANDLED;
  }

}
