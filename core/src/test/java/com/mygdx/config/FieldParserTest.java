package com.mygdx.config;

import com.mygdx.model.Field;
import com.mygdx.model.GameContent;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


class FieldParserTest {

    @Test
    @DisplayName("shouldReturnCorrectField")
    public void shouldReturnCorrectField(){
        // given
        String filesPath = "src/test/resources/fieldParser";

        FilesLoader loader = new FilesLoader(filesPath);
        FieldParser parser = new FieldParser(filesPath);
        GameContent content = new GameContent();

        List<String> loaderFiles = loader.loadJsonFileNames();

        Field exampleField = new Field(1, "field", "pole", "field.g3db");

        // when
        parser.parseContent(content, loaderFiles);
        Field singleField = content.getSingleField(0);

        // then
        assertThat(singleField).isEqualToComparingFieldByField(exampleField);
    }

}