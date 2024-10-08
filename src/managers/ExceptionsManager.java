package managers;

import java.io.IOException;

public class ExceptionsManager extends RuntimeException {

    public ExceptionsManager(String message, IOException cause) {
        super(message, cause);
    }

    public ExceptionsManager(String message) {
        super(message);
    }
}
