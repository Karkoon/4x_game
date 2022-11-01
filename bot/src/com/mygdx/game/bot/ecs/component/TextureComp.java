package com.mygdx.game.bot.ecs.component;

import com.artemis.PooledComponent;
import com.badlogic.gdx.graphics.Texture;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
public class TextureComp extends PooledComponent {

  private @NonNull Texture texture;

  public void setTexture(Texture texture) {
    this.texture = texture;
  }

  @Override
  protected void reset() {
    this.texture.dispose();
    this.texture = null;
  }
}
