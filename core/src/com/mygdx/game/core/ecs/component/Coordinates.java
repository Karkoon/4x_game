package com.mygdx.game.core.ecs.component;

import com.artemis.PooledComponent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coordinates extends PooledComponent {

  private int x;
  private int y;

  @Override
  protected void reset() {
    setCoordinates(0, 0);
  }

  public void setCoordinates(int x, int y) {
    this.x = x;
    this.y = y;
  }

  //TODO: Nie działa mi overload metody
//  public void setCoordinates(Coordinates destination) {
//    this.x = destination.getX();
//    this.y = destination.getY();
//  }

  public void setCoordinates(@NonNull Coordinates other) {
    setCoordinates(other.x, other.y);
  }
}
