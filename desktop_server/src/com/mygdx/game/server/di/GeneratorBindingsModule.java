package com.mygdx.game.server.di;

import com.mygdx.game.server.initialize.field_generators.ArcticFieldMapGenerator;
import com.mygdx.game.server.initialize.field_generators.RandomFieldMapGenerator;
import com.mygdx.game.server.initialize.field_generators.DesertFieldMapGenerator;
import com.mygdx.game.server.initialize.field_generators.MapGenerator;
import com.mygdx.game.server.initialize.field_generators.StableFieldMapGenerator;
import com.mygdx.game.server.initialize.subfield_generators.ArcticBasicSubfieldMapGenerator;
import com.mygdx.game.server.initialize.subfield_generators.ArcticForestSubfieldMapGenerator;
import com.mygdx.game.server.initialize.subfield_generators.ArcticMountainsSubfieldMapGenerator;
import com.mygdx.game.server.initialize.subfield_generators.ArcticRiverSubfieldMapGenerator;
import com.mygdx.game.server.initialize.subfield_generators.DesertBasicSubfieldMapGenerator;
import com.mygdx.game.server.initialize.subfield_generators.DesertForestSubfieldMapGenerator;
import com.mygdx.game.server.initialize.subfield_generators.DesertMountainsSubfieldMapGenerator;
import com.mygdx.game.server.initialize.subfield_generators.DesertRiverSubfieldMapGenerator;
import com.mygdx.game.server.initialize.subfield_generators.RandomSubfieldMapGenerator;
import com.mygdx.game.server.initialize.subfield_generators.StableBasicSubfieldMapGenerator;
import com.mygdx.game.server.initialize.subfield_generators.StableForestSubfieldMapGenerator;
import com.mygdx.game.server.initialize.subfield_generators.StableMountainsSubfieldMapGenerator;
import com.mygdx.game.server.initialize.subfield_generators.StableRiverSubfieldMapGenerator;
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

  @Binds
  @IntoSet
  SubfieldMapGenerator provideArcticForestSubfieldMapGenerator(
      ArcticForestSubfieldMapGenerator generator
  );

  @Binds
  @IntoSet
  SubfieldMapGenerator provideArcticMountainsSubfieldMapGenerator(
      ArcticMountainsSubfieldMapGenerator generator
  );

  @Binds
  @IntoSet
  SubfieldMapGenerator provideArcticRiverSubfieldMapGenerator(
      ArcticRiverSubfieldMapGenerator generator
  );

  @Binds
  @IntoSet
  SubfieldMapGenerator provideDesertBasicSubfieldMapGenerator(
      DesertBasicSubfieldMapGenerator generator
  );

  @Binds
  @IntoSet
  SubfieldMapGenerator provideDesertForestSubfieldMapGenerator(
      DesertForestSubfieldMapGenerator generator
  );

  @Binds
  @IntoSet
  SubfieldMapGenerator provideDesertMountainsSubfieldMapGenerator(
      DesertMountainsSubfieldMapGenerator generator
  );

  @Binds
  @IntoSet
  SubfieldMapGenerator provideDesertRiverSubfieldMapGenerator(
      DesertRiverSubfieldMapGenerator generator
  );

  @Binds
  @IntoSet
  SubfieldMapGenerator provideStableBasicSubfieldMapGenerator(
      StableBasicSubfieldMapGenerator generator
  );

  @Binds
  @IntoSet
  SubfieldMapGenerator provideStableForestSubfieldMapGenerator(
      StableForestSubfieldMapGenerator generator
  );

  @Binds
  @IntoSet
  SubfieldMapGenerator provideStableMountainsSubfieldMapGenerator(
      StableMountainsSubfieldMapGenerator generator
  );

  @Binds
  @IntoSet
  SubfieldMapGenerator provideStableRiverSubfieldMapGenerator(
      StableRiverSubfieldMapGenerator generator
  );
}
