package com.mygdx.game.client.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector3;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TextureDraw {

  private int entityId;
  private Texture texture;
  private Vector3 position;

}
