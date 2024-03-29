package com.mygdx.game.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class MapTypeConfig implements Config {

  private long id;
  private String name;
  private String polishName;
  private String textureName; // icon

  @Override
  public String toString() {
    return name + " (" + id + ")";
  }
}
