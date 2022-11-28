package com.mygdx.game.server.ecs.system;

import com.artemis.ComponentMapper;
import com.artemis.annotations.All;
import com.artemis.annotations.One;
import com.artemis.systems.IteratingSystem;
import com.mygdx.game.core.ecs.component.Field;
import com.mygdx.game.core.ecs.component.Name;
import com.mygdx.game.core.ecs.component.Owner;
import com.mygdx.game.core.ecs.component.Unit;
import com.mygdx.game.server.di.GameInstanceScope;
import com.mygdx.game.server.model.Client;
import com.mygdx.game.server.model.GameInstance;
import com.mygdx.game.server.model.GameRoom;
import com.mygdx.game.server.network.gameinstance.services.WinAnnouncementService;
import dagger.Lazy;
import lombok.extern.java.Log;

import javax.inject.Inject;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Log
@All({Owner.class})
@One({Unit.class, Field.class})
@GameInstanceScope
public class LoseCheckSystem extends IteratingSystem {

  private final WinAnnouncementService announcementService;
  private final Lazy<GameInstance> gameInstance;
  private final GameRoom gameRoom;
  private Set<String> currentPlayers;
  private ComponentMapper<Owner> ownerComponentMapper;
  private ComponentMapper<Name> nameMapper;

  @Inject
  public LoseCheckSystem(
      WinAnnouncementService announcementService,
      Lazy<GameInstance> gameInstance,
      GameRoom gameRoom
  ) {
    this.announcementService = announcementService;
    this.gameInstance = gameInstance;
    this.gameRoom = gameRoom;
  }

  @Override
  protected void begin() {
    this.currentPlayers = new HashSet<>();
  }

  @Override
  protected void process(int entityId) {
    var token = ownerComponentMapper.get(entityId).getToken();
    currentPlayers.add(token);
    log.info("processing owner: " + nameMapper.get(entityId).getName() + " id " + entityId);
  }

  @Override
  protected void end() {
    if (currentPlayers.size() == 1 && gameRoom.getClients().size() != 1) {
      currentPlayers.stream()
          .findFirst()
          .ifPresent(winner -> {
            log.info("game ended, the winner is " + winner);
            announcementService.notifyOfWinner(winner);
          });
    } else {
      var losers = gameRoom.getClients().stream().map(Client::getPlayerToken)
          .filter(token -> !currentPlayers.contains(token))
          .collect(Collectors.toUnmodifiableSet());
      gameInstance.get().notifyLosers(losers);
      gameInstance.get().setLoserTokens(losers);
    }
  }
}
