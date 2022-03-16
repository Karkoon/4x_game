package com.mygdx.model;

import java.util.ArrayList;
import java.util.List;

public class GameContent {

    private List<Field> fieldList;

    public GameContent() {
        this.fieldList = new ArrayList<>();
    }

    public Field getSingleField(int index) {
        if(fieldList.size() > index)
            return fieldList.get(index);
        return null;
    }

    public void addField(Field field) {
        this.fieldList.add(field);
    }
}
