package com.mygdx.game.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class CivilizationConfig implements Config {

  private long id;
  @NonNull
  private String name;
  @NonNull
  private String polishName;
  private int startingUnit;

  @Override
  public String toString() {
    return name;
  }
}
