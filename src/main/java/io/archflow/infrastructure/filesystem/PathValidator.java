package io.archflow.infrastructure.filesystem;

import java.nio.file.Path;

/**
 * Validates filesystem paths before any archive operation begins.
 */
public class PathValidator {

    public boolean exists(Path path) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public boolean isReadable(Path path) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public boolean isDirectory(Path path) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }

    public boolean destinationAlreadyExists(Path path) {
        // TODO: implement
        throw new UnsupportedOperationException("Not implemented yet");
    }
}
