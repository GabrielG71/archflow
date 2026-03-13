package io.archflow.infrastructure.filesystem;

import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Validates filesystem paths before any archive operation begins.
 */
public class PathValidator {

    public boolean exists(Path path) {
        return Files.exists(path);
    }

    public boolean isReadable(Path path) {
        return Files.isReadable(path);
    }

    public boolean isDirectory(Path path) {
        return Files.isDirectory(path);
    }

    public boolean destinationAlreadyExists(Path path) {
        return Files.exists(path);
    }
}
