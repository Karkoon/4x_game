package com.mygdx.game.client_core.model;

import dagger.assisted.Assisted;
import dagger.assisted.AssistedFactory;
import dagger.assisted.AssistedInject;

public class GameRoom {

  @AssistedInject
  public GameRoom(
      @Assisted String gameRoomName
  ) {
    super();
  }

  @AssistedFactory
  public interface Factory {
    GameRoom get(
        @Assisted String gameRoomName
    );
  }
}
