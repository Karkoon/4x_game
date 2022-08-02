package com.mygdx.game.core.util;

import lombok.Data;

@Data
public class DirtyInt {
  private int value = -0xC0FFE;
  private boolean isDirty = true;
}
