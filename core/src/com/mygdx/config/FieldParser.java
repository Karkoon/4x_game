package com.mygdx.config;

import com.mygdx.model.Field;
import com.mygdx.model.GameContent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

class FieldParser implements JsonParser {

    private String directoryPath;

    public FieldParser(String directoryPath) {
        this.directoryPath = directoryPath;
    }
    @Override
    public void parseContent(GameContent content, String fileName){
        JSONParser parser = new JSONParser();

        try (Reader reader = new FileReader(directoryPath + "/" + fileName)) {
            JSONObject jsonObject = (JSONObject) parser.parse(reader);
            Field field = parseField(jsonObject);
            content.addField(field);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Field parseField(JSONObject jsonObject) {
        int id = ((Long) jsonObject.get("id")).intValue();
        String name = (String) jsonObject.get("name");
        String polishName = (String) jsonObject.get("polishName");
        String resourceName = (String) jsonObject.get("resourceName");

        return new Field(id, name, polishName, resourceName);
    }


}
