package goorm.brainsnack.quiz.domain;

import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.EnumType.*;

@Getter
@Builder
@Entity
@Table(name = "Quiz_TB")
@AllArgsConstructor(access= AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Quiz {

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

    protected Quiz(int quizNum, String title, String example,
                String choiceFirst, String choiceSecond, String choiceThird,
                String choiceFourth, String choiceFifth, int answer, String solution) {
        this.quizNum = quizNum;
        this.example = example;
        this.title = title;
        this.choiceFirst = choiceFirst;
        this.choiceSecond = choiceSecond;
        this.choiceThird = choiceThird;
        this.choiceFourth = choiceFourth;
        this.choiceFifth = choiceFifth;
        this.answer = answer;
        this.solution = solution;
    }

    public static Quiz of(int quizNum, String title, String example,
                                      String choiceFirst, String choiceSecond, String choiceThird,
                                      String choiceFourth, String choiceFifth, int answer, String solution, Boolean isSimilar) {
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
                .build();
    }
}
