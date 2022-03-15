package com.mygdx.config;

import com.mygdx.model.Field;
import com.mygdx.model.GameContent;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class FieldParser implements JsonParser {

    private final String FIELD_DIR = "assets/fields";

    @Override
    public void loadContent(GameContent content) throws IOException {
        List<Field> fieldList = new ArrayList<>();

//        List<String> jsonFileNames = loadJsonFileNames();
    }


}
