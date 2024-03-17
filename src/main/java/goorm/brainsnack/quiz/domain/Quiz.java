package goorm.brainsnack.quiz.domain;

import jakarta.persistence.*;
import lombok.*;

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
    private String solution;
}
