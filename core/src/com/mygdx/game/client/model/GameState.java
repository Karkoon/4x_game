package com.mygdx.game.client.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

@Singleton
@Setter
@Getter
public class GameState {

    private List<Field> fieldList;

    @Inject
    public GameState() {
    }
}
