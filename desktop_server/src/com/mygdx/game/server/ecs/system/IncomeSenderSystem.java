package com.mygdx.game.server.ecs.system;

import com.artemis.BaseSystem;
import com.mygdx.game.core.network.messages.MaterialIncomeMessage;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.model.GameRoom;
import com.mygdx.game.server.network.MessageSender;
import com.mygdx.game.server.util.MaterialUtilServer;
import dagger.Lazy;
import lombok.extern.java.Log;

import javax.inject.Inject;
import java.util.HashMap;

@Log
@GameInstanceScope
public class IncomeSenderSystem extends BaseSystem {

  private final GameRoom gameRoom;
  private final Lazy<MaterialUtilServer> materialUtilServer;
  private final MessageSender messageSender;

  @Inject
  public IncomeSenderSystem(
      final GameRoom gameRoom,
      final Lazy<MaterialUtilServer> materialUtilServer,
      final MessageSender messageSender
  ) {
    this.gameRoom = gameRoom;
    this.materialUtilServer = materialUtilServer;
    this.messageSender = messageSender;
  }

  @Override
  protected void processSystem() {
    var clients = gameRoom.getClients();
    var incomes = materialUtilServer.get().calculateIncomes();
    for (var client : clients) {
      var playerIncomes = incomes.get(client.getPlayerToken());
      // fix crash when the income for the given key could be null
      if (playerIncomes == null) {
        return;
      }
      var incomesNetwork = new HashMap<String, Integer>();
      for (var materialBaseIntegerEntry : playerIncomes.entrySet()) {
        incomesNetwork.put(materialBaseIntegerEntry.getKey().name(), materialBaseIntegerEntry.getValue());
      }
      var msg = new MaterialIncomeMessage(incomesNetwork);
      messageSender.send(msg, client);
    }
  }

}
