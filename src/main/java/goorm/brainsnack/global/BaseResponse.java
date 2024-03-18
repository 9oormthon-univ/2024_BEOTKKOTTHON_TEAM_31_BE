package goorm.brainsnack.global;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import goorm.brainsnack.exception.ErrorCode;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

import static goorm.brainsnack.exception.ErrorCode.SUCCESS;

@Getter
@JsonPropertyOrder({"time", "status", "code", "message", "result"})
public class BaseResponse<T> {

    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private final LocalDateTime time = LocalDateTime.now();
    private final HttpStatus status;
    private final String code;
    private final String message;


    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T result;


    public BaseResponse(T result) {
        this.status = SUCCESS.getStatus();
        this.code = SUCCESS.getCode();
        this.message = SUCCESS.getMessage();
        this.result = result;
    }


    //요청에 실패시
    public BaseResponse(ErrorCode code) {
        this.status = code.getStatus();
        this.code = code.getCode();
        this.message = code.getMessage();
    }

    //생성 메서드
//    public static BaseResponse of(ErrorCode e) {
//        return BaseResponse.builder()
//                .status(e.getStatus())
//                .code(e.getCode())
//                .message(e.getMessage())
//                .build();
//    }
}
