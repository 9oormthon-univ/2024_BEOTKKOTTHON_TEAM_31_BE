package goorm.brainsnack.quiz.service;

import goorm.brainsnack.exception.ErrorCode;
import goorm.brainsnack.exception.QuizException;
import goorm.brainsnack.quiz.domain.Quiz;
import goorm.brainsnack.quiz.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;

    @Override
    public Quiz findQuiz(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new QuizException(ErrorCode.NOT_EXIST_QUIZ));
        // 여기서 DTO 로 반환해서 Controller 에게 넘겨주기
        return quiz;
    }
}
