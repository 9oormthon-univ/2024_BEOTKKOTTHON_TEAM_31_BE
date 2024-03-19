package goorm.brainsnack.quiz.service;

import goorm.brainsnack.quiz.domain.Quiz;
import goorm.brainsnack.quiz.domain.QuizCategory;
import goorm.brainsnack.quiz.dto.QuizResponseDto.CategoryQuizListDto;
import goorm.brainsnack.quiz.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static goorm.brainsnack.quiz.dto.QuizResponseDto.QuizDetailDto;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;

    @Override
    public CategoryQuizListDto getCategoryQuizList(String categoryName) {
        QuizCategory category = QuizCategory.getInstance(categoryName);

        List<Quiz> quizList = quizRepository.findAllByCategory(category);

        return CategoryQuizListDto.builder()
                .size(quizList.size())
                .quizDetailDtoList(quizList.stream()
                        .map(QuizDetailDto::from)
                        .toList())
                .build();
    }
}
