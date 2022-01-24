package com.amazing.candlestick;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import org.springframework.core.io.ClassPathResource;

public class TestHelper {

    public static String loadJsonFromFile(String fileName) throws IOException {
        File resource = new ClassPathResource("mockData/" + fileName + ".json").getFile();
        return new String(Files.readAllBytes(resource.toPath()));
    }

}
