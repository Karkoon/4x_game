package com.mygdx.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
public class Field {

    private int id;
    private String name;
    private String polishName;
    private String resourceName;

    public static String FIELD_PREVIEW = "fields/";

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Field field = (Field) o;
        return id == field.id && Objects.equals(name, field.name) && Objects.equals(polishName, field.polishName) && Objects.equals(resourceName, field.resourceName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, polishName, resourceName);
    }
}
