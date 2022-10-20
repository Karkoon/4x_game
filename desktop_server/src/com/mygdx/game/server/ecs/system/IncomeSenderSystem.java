package com.mygdx.game.server.ecs.system;

import com.artemis.BaseSystem;
import com.mygdx.game.core.network.messages.ChangeTurnMessage;
import com.mygdx.game.core.network.messages.MaterialIncomeMessage;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.model.GameRoom;
import com.mygdx.game.server.network.MessageSender;
import com.mygdx.game.server.util.MaterialUtilServer;

import javax.inject.Inject;
import java.util.List;

@GameInstanceScope
public class IncomeSenderSystem extends BaseSystem {

  private final GameRoom gameRoom;
  private final MaterialUtilServer materialUtilServer;
  private final MessageSender messageSender;

  @Inject
  public IncomeSenderSystem(
      final GameRoom gameRoom,
      final MaterialUtilServer materialUtilServer,
      final MessageSender messageSender
  ) {
    this.gameRoom = gameRoom;
    this.materialUtilServer = materialUtilServer;
    this.messageSender = messageSender;
  }

  @Override
  protected void processSystem() {
    var clients = gameRoom.getClients();
    var incomes = materialUtilServer.calculateIncomes();
    for (Client client : clients) {
      var msg = new MaterialIncomeMessage(incomes.get(client.getPlayerToken()));
      messageSender.send(msg, client);
    }
  }

}
