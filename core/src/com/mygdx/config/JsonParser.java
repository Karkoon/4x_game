package com.mygdx.config;

import com.mygdx.model.GameContent;

import java.io.IOException;

interface JsonParser {

    void loadContent(GameContent content) throws IOException;

}
