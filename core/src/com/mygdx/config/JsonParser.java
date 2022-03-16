package com.mygdx.config;

import com.mygdx.model.GameContent;

import java.io.File;
import java.io.IOException;

interface JsonParser {

    void parseContent(GameContent content, String fileName) throws IOException;

}
