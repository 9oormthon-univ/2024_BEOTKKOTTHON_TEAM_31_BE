package goorm.brainsnack.quiz.domain;

import goorm.brainsnack.quiz.dto.QuizResponseDto;
import goorm.brainsnack.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.EnumType.*;

@Getter
@Builder
@Entity
@Table(name = "Quiz_TB")
@AllArgsConstructor(access= AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Quiz extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "quiz_id")
    private Long id;
    private int quizNum;

    @Enumerated(STRING)
    private QuizCategory category;
    private Boolean isSimilar;

    private String title;
    private String example;
    private String choiceFirst;
    private String choiceSecond;
    private String choiceThird;
    private String choiceFourth;
    private String choiceFifth;

    private int answer;

    @Column(length = 500)
    private String solution;

    public static Quiz of(int quizNum, String title, String example,
                                      String choiceFirst, String choiceSecond, String choiceThird,
                                      String choiceFourth, String choiceFifth, int answer,
                                      String solution, Boolean isSimilar, QuizCategory category) {
        return Quiz.builder()
                .quizNum(quizNum)
                .title(title)
                .example(example)
                .choiceFirst(choiceFirst)
                .choiceSecond(choiceSecond)
                .choiceThird(choiceThird)
                .choiceFourth(choiceFourth)
                .choiceFifth(choiceFifth)
                .answer(answer)
                .solution(solution)
                .isSimilar(isSimilar)
                .category(category)
                .build();
    }

    public static QuizResponseDto.QuizDto toQuizDto(Quiz quiz) {
        return QuizResponseDto.QuizDto.builder()
                .title(quiz.getTitle())
                .example(quiz.getExample())
                .choiceFirst(quiz.getChoiceFirst())
                .choiceSecond(quiz.getChoiceSecond())
                .choiceThird(quiz.getChoiceThird())
                .choiceFourth(quiz.getChoiceFourth())
                .choiceFifth(quiz.getChoiceFifth())
                .answer(quiz.getAnswer())
                .category(quiz.getCategory())
                .solution(quiz.getSolution())
                .build();
    }
}
