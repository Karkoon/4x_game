package com.mygdx.config;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

public class FilesLoader {

    private String directoryPath;

    public FilesLoader(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    public List<String> loadJsonFileNames() throws IOException {
        return Files.list(new File(directoryPath).toPath())
                .map(path -> path.getFileName().toString())
                .filter(path -> path.endsWith(".json"))
                .collect(Collectors.toList());
    }

}
