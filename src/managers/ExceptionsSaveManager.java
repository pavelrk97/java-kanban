package managers;

import java.io.IOException;

public class ExceptionsSaveManager extends RuntimeException {

    public ExceptionsSaveManager(String message, IOException cause) {
        super(message, cause);
    }

    public ExceptionsSaveManager(String message) {
        super(message);
    }
}
