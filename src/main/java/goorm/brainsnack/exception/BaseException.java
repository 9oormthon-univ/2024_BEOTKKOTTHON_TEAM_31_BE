package goorm.brainsnack.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class BaseException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    protected BaseException(HttpStatus status, String code, String message) {
        this.message = message;
        this.code = code;
        this.httpStatus = status;
    }
}
