package goorm.brainsnack.quiz.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "QuizData_TB")
@AllArgsConstructor(access= AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuizData {

    @Id @GeneratedValue
    @Column(name = "quizData_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;
    private int quizParticipantsNum;
    private int correctAnswerNum;
}
