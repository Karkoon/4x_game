package com.mygdx.game.server.di;

import com.mygdx.game.server.initialize.subfield_generators.BasicSubfieldMapGenerator;
import com.mygdx.game.server.initialize.field_generators.BotWinningFieldMapGenerator;
import com.mygdx.game.server.initialize.field_generators.MapGenerator;
import com.mygdx.game.server.initialize.subfield_generators.DesertSubfieldMapGenerator;
import com.mygdx.game.server.initialize.subfield_generators.SubfieldMapGenerator;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoSet;

@Module
@GameInstanceScope
public interface GeneratorBindingsModule {
  @Binds
  @IntoSet
  @GameInstanceScope
  MapGenerator provideBotWinningFieldMapGenerator(
      BotWinningFieldMapGenerator generator
  );

  @Binds
  @IntoSet
  @GameInstanceScope
  SubfieldMapGenerator provideBasicSubfieldMapGenerator(
      BasicSubfieldMapGenerator generator
  );

  @Binds
  @IntoSet
  SubfieldMapGenerator provideDesertSubfieldMapGenerator(
          DesertSubfieldMapGenerator generator
  );
}
