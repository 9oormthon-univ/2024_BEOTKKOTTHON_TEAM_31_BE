package goorm.brainsnack.exception;

public class QuizException extends BaseException{
    public QuizException(ErrorCode e) {
        super(e.getStatus(),e.getCode(),e.getMessage());
    }
}
