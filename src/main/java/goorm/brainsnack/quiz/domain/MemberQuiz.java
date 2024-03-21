package goorm.brainsnack.quiz.domain;

import goorm.brainsnack.global.BaseEntity;
import goorm.brainsnack.member.domain.Member;
import goorm.brainsnack.member.dto.MemberResponseDto;
import goorm.brainsnack.quiz.dto.MemberQuizResponseDto;
import goorm.brainsnack.quiz.dto.SimilarQuizResponseDto;
import goorm.brainsnack.quiz.dto.QuizRequestDto;
import jakarta.persistence.*;
import lombok.*;

import static goorm.brainsnack.quiz.dto.QuizRequestDto.*;

import java.util.List;

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

    public static MemberQuizResponseDto.MemberQuizDto getMemberQuizDto(MemberQuiz memberQuiz) {
        return MemberQuizResponseDto.MemberQuizDto.builder()
                .quizNum(memberQuiz.getQuiz().getQuizNum())
                .quizId(memberQuiz.getQuiz().getId())
                .build();
    }

    public static MemberQuizResponseDto.MemberQuizDto getMemberSimilarQuizDto(MemberQuiz memberQuiz , int count) {
        return MemberQuizResponseDto.MemberQuizDto.builder()
                .quizNum(count)
                .quizId(memberQuiz.getQuiz().getId())
                .build();
    }

    public static SimilarQuizResponseDto.MemberSimilarQuizDto getMemberSimilarQuizListDto(MemberResponseDto.MemberDto member ,
                                                                                          List<MemberQuizResponseDto.MemberQuizDto> result) {
        return SimilarQuizResponseDto.MemberSimilarQuizDto.builder()
                .memberId(member.getId())
                .entryCode(member.getEntryCode())
                .createSimilarQuizCount(result.size())
                .memberQuizList(result)
                .build();
    }

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
