package com.mygdx.game.server.di;

import com.mygdx.game.server.initialize.generators.BasicSubfieldMapGenerator;
import com.mygdx.game.server.initialize.generators.BotWinningFieldMapGenerator;
import com.mygdx.game.server.initialize.generators.MapGenerator;
import com.mygdx.game.server.initialize.generators.SubfieldMapGenerator;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoSet;

@Module
public interface GeneratorBindingsModule {
  @Binds
  @IntoSet
  MapGenerator provideBotWinningFieldMapGenerator(
      BotWinningFieldMapGenerator generator
  );

  @Binds
  @IntoSet
  SubfieldMapGenerator provideBasicSubfieldMapGenerator(
      BasicSubfieldMapGenerator generator
  );
}
