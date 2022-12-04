package com.mygdx.game.core.model;

public enum TechnologyImpactParameter {

  // unit parameters
  HP("HP"),
  ATTACK_POWER("Attack power"),
  DEFENSE("Defense"),
  SIGHT_RADIUS("Sight radius"),
  MOVE_RANGE("Move range"),
  ATTACK_RANGE("Attack range"),

  // building parameters
  TIME("Building time"),

  // building & material parameters
  GOLD("Gold"),
  FOOD("Food"),
  SCIENCE("Science"),
  PRODUCTION("Production");

  public final String name;


  TechnologyImpactParameter(String name) {
    this.name = name;
  }
}
