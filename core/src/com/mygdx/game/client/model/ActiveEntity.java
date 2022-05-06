package com.mygdx.game.client.model;

import lombok.NoArgsConstructor;

import javax.inject.Singleton;
import javax.swing.text.html.parser.Entity;

@Singleton
@NoArgsConstructor
public class ActiveEntity {

  private ActiveEntityType entityType;
  private Entity entity;


}
