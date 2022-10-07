package com.mygdx.game.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MapTypeConfig implements Config {

  private long id;
  private String name;
  private String polishName;
  private String textureName; // icon
}
