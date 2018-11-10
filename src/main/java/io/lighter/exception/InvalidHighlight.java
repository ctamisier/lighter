package io.lighter.exception;

public class InvalidHighlight extends RuntimeException {
    public InvalidHighlight() {
        super("Invalid highlight giving null or empty 'hl'");
    }
}
