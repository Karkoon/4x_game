package com.mygdx.game.core.model;

public enum MaterialBase {

  GOLD("Gold", "Żywność", "materials/gold_icon.png"),
  FOOD("Food", "Jedzenie", "materials/food_icon.png"),
  SCIENCE("Science", "Nauka", "materials/science_icon.png"),
  PRODUCTION("Production", "Produkcja", "materials/production_icon.png");

  public final String name;
  public final String polishName;
  public final String iconPath;

  MaterialBase(String name, String polishName, String iconPath) {
    this.name = name;
    this.polishName = polishName;
    this.iconPath = iconPath;
  }
}
