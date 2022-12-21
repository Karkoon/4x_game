package com.mygdx.game.server.di;

import com.mygdx.game.server.initialize.field_generators.ArcticFieldMapGenerator;
import com.mygdx.game.server.initialize.field_generators.RandomFieldMapGenerator;
import com.mygdx.game.server.initialize.field_generators.DesertFieldMapGenerator;
import com.mygdx.game.server.initialize.field_generators.MapGenerator;
import com.mygdx.game.server.initialize.field_generators.StableFieldMapGenerator;
import com.mygdx.game.server.initialize.subfield_generators.ArcticBasicSubfieldMapGenerator;
import com.mygdx.game.server.initialize.subfield_generators.RandomSubfieldMapGenerator;
import com.mygdx.game.server.initialize.subfield_generators.SubfieldMapGenerator;
import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoSet;

@Module
public interface GeneratorBindingsModule {

  @Binds
  @IntoSet
  @GameInstanceScope
  MapGenerator provideRandomFieldMapGenerator(
      RandomFieldMapGenerator generator
  );

  @Binds
  @IntoSet
  @GameInstanceScope
  MapGenerator provideArcticFieldMapGenerator(
      ArcticFieldMapGenerator generator
  );

  @Binds
  @IntoSet
  @GameInstanceScope
  MapGenerator provideDesertFieldMapGenerator(
      DesertFieldMapGenerator generator
  );

  @Binds
  @IntoSet
  @GameInstanceScope
  MapGenerator provideStableFieldMapGenerator(
      StableFieldMapGenerator generator
  );

  @Binds
  @IntoSet
  @GameInstanceScope
  SubfieldMapGenerator provideBasicSubfieldMapGenerator(
      RandomSubfieldMapGenerator generator
  );

  @Binds
  @IntoSet
  SubfieldMapGenerator provideArcticBasicSubfieldMapGenerator(
      ArcticBasicSubfieldMapGenerator generator
  );
}
