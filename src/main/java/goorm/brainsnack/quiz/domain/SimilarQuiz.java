package goorm.brainsnack.quiz.domain;

import goorm.brainsnack.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

import static jakarta.persistence.EnumType.STRING;

@Getter
@Builder
@Entity
@Table(name = "SimilarQuiz_TB")
@AllArgsConstructor(access= AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SimilarQuiz extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "similarQuiz_id")
    private Long id;
    private int quizNum;

    @Enumerated(STRING)
    private QuizCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;

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
