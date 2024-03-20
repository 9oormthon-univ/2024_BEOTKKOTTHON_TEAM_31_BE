package goorm.brainsnack.quiz.domain;

import goorm.brainsnack.global.BaseEntity;
import goorm.brainsnack.member.domain.Member;
import goorm.brainsnack.quiz.dto.QuizRequestDto;
import jakarta.persistence.*;
import lombok.*;

import static goorm.brainsnack.quiz.dto.QuizRequestDto.*;

@Getter
@Builder
@Entity
@Table(name = "MemberQuiz_TB")
@AllArgsConstructor(access= AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberQuiz extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memberQuiz_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;
    private Boolean isCorrect;
    private int choice;

    public static MemberQuiz of(SingleGradeRequestDto request, Member member, Quiz quiz) {
        boolean userCorrect = false;
        if (quiz.getAnswer() == request.getChoice()) {
            userCorrect = true;
        }

        return MemberQuiz.builder()
                .member(member)
                .quiz(quiz)
                .isCorrect(userCorrect)
                .choice(request.getChoice())
                .build();
    }
}
