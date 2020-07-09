package guru.springframework.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class SaveException extends RuntimeException {
    public SaveException() { super(); }

    public SaveException(String message) {
        super(message);
    }

    public SaveException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
