package goorm.brainsnack.quiz.domain;

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
}
