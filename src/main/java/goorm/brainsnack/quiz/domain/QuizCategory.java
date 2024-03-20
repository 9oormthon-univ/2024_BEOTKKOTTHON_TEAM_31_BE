package goorm.brainsnack.quiz.domain;

import goorm.brainsnack.exception.BaseException;
import goorm.brainsnack.exception.ErrorCode;

public enum QuizCategory {
    LANG , MATH , DEDUCE, SPATIAL;

    public static QuizCategory getInstance(String name) {
        for (QuizCategory value : QuizCategory.values()) {
            if (value.name().equals(name)){
                return value;
            }
        }

        throw new BaseException(ErrorCode.NOT_EXIST_CATEGORY);
    }
}
