package goorm.brainsnack.quiz.domain;

import goorm.brainsnack.global.BaseEntity;
import goorm.brainsnack.member.domain.Member;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "MemberQuiz_TB")
@AllArgsConstructor(access= AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberQuiz extends BaseEntity {

    @Id @GeneratedValue
    @Column(name = "memberQuiz_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;
    private Boolean isCorrect;
    private int choice;
}
