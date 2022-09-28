package com.mygdx.game.core.ecs.component;

import com.artemis.PooledComponent;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoveRange extends PooledComponent {
    private int moveRange;
    private int currentRange;

    @Override
    protected void reset() {
        this.currentRange = this.moveRange;
    }
}
