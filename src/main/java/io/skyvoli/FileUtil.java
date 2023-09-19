package io.skyvoli;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileUtil {

    private static final Logger logger = LogManager.getRootLogger();

    public static void saveIntoFile(Object input, String filename) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Path filePath = Path.of("out", filename);
            Path dirPath = Path.of("", "out");

            if (!Files.exists(dirPath)) {
                Files.createDirectory(dirPath);
            }

            if (!Files.exists(filePath)) {
                Files.createFile(filePath);
            }

            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(input);
            Files.writeString(filePath, json);
        } catch (IOException e) {
            logger.error("Failed to save object into file");
        }
    }
}

