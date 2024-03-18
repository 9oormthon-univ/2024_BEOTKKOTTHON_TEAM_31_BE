package goorm.brainsnack.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class BaseException extends RuntimeException {
    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    public BaseException(ErrorCode code) {
        this.message = code.getMessage();
        this.code = code.getCode();
        this.httpStatus = code.getStatus();
    }
}
