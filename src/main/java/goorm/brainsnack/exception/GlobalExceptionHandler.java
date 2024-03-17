package goorm.brainsnack.exception;

import goorm.brainsnack.global.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Objects;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<BaseResponse> BaseException(BaseException e) {
        e.printStackTrace();
        ErrorCode errorCode = ErrorCode.findByMessage(e.getMessage());
        return ResponseEntity.status(e.getHttpStatus())
                .body(BaseResponse.of(Objects.requireNonNull(errorCode)));
    }
}
