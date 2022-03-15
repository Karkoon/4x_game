package com.mygdx.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


class FilesLoaderTest {

    @Test
    @DisplayName("shouldReturnCorrectJsonFiles")
    void shouldReturnCorrectJsonFiles() throws IOException {
        // given
        String filesPath = "src/test/resources/fieldFilesLoader";
        FilesLoader loader = new FilesLoader(filesPath);
        List<String> correctFiles = new ArrayList<>();
        correctFiles.add("a.json");
        correctFiles.add("b.json");

        // when
        List<String> loaderFiles = loader.loadJsonFileNames();

        // then
        assertEquals(correctFiles, loaderFiles);
    }

}