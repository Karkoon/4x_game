package com.mygdx.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Field {

    private int id;
    private String name;
    private String polishName;
    private String resourcePath;

}
