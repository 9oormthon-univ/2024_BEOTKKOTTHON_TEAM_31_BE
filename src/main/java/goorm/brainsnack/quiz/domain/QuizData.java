package goorm.brainsnack.quiz.domain;

import goorm.brainsnack.global.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Builder
@Entity
@Table(name = "QuizData_TB")
@AllArgsConstructor(access= AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizData extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "quizData_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;
    private int quizParticipantsCounts;
    private int correctAnswerCounts;

    //생성 메서드
    public static QuizData from(Quiz quiz) {
        return QuizData.builder()
                .quiz(quiz)
                .quizParticipantsCounts(0)
                .correctAnswerCounts(0)
                .build();
    }

    //비즈니스 메서드
    public void updateQuizData(MemberQuiz memberQuiz) {
        if (memberQuiz.getIsCorrect()) {
            this.correctAnswerCounts++;
        }
        this.quizParticipantsCounts++;
    }
}
