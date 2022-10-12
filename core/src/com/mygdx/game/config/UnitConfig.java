package com.mygdx.game.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UnitConfig implements Config, ModelConfig {

  private long id;
  @NonNull
  private String name;
  @NonNull
  private String polishName;
  @NonNull
  private String modelPath;
  @NonNull
  private String textureName;
  @NonNull
  private String iconName;
  private int maxHp;
  private int attackPower;
  private int defense;
  private int sightRadius;
  private int moveRange;
  private int attackRange;
}
