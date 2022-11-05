package com.mygdx.game.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CivilizationConfig implements Config {

  private long id;
  @NonNull
  private String name;
  @NonNull
  private String polishName;

  @Override
  public long getId() {
    return 0;
  }
}
