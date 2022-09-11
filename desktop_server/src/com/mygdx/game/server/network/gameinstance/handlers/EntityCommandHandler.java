package com.mygdx.game.server.network.gameinstance.handlers;

import com.mygdx.game.core.ecs.component.Owner;
import com.mygdx.game.server.model.Client;
import lombok.extern.java.Log;

@Log
public abstract class EntityCommandHandler {

  protected boolean checkValidity(String[] commands, Client client) {
    var ownerMapper = client.getGameRoom().getGameInstance().getWorld().getMapper(Owner.class);
    var entityId = Integer.parseInt(commands[1]);
    if (!ownerMapper.get(entityId).getToken().equals(client.getPlayerToken())) {
      log.info("player " + client.getPlayerUsername() + " tried to change anothers entity");
      return false;
    }
    return true;
  }
}
