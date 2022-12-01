package com.mygdx.game.core.model;

import java.util.function.BiFunction;

public enum ImpactOperation {

  PLUS(Float::sum, "+"),
  MINUS((x,y) -> x - y, "-"),
  MULTIPLE((x,y) -> x * y, "*"),
  DIVIDE((x,y) -> x / y, "/"),
  NONE((x,y) -> null, "");

  public final BiFunction<Float, Float, Float> function;
  public final String name;

  ImpactOperation(
    BiFunction<Float, Float, Float> function,
    String name
  ) {
    this.function = function;
    this.name = name;
  }

}
