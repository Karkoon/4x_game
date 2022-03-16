package com.mygdx.config;

import com.mygdx.model.GameContent;

import java.io.File;
import java.io.IOException;
import java.util.List;

interface JsonParser {

    void parseContent(GameContent content, List<String> fileNames) throws IOException;

}
