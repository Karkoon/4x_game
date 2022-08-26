package com.mygdx.game.server.di;

import com.mygdx.game.server.model.GameInstance;
import com.mygdx.game.server.model.GameRoom;
import dagger.BindsInstance;
import dagger.Subcomponent;

@Subcomponent(
    modules = {
        WorldModule.class,
        GeneratorBindingsModule.class
    }
)
@GameInstanceScope
public interface GameInstanceSubcomponent {
  GameInstance get();

  @Subcomponent.Builder
  interface Builder {
    @BindsInstance
    Builder gameRoom(GameRoom gameRoom);

    GameInstanceSubcomponent build();
  }
}
