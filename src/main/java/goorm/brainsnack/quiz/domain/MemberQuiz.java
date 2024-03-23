package goorm.brainsnack.quiz.domain;

import goorm.brainsnack.global.BaseEntity;
import goorm.brainsnack.member.domain.Member;
import goorm.brainsnack.member.dto.MemberResponseDto;
import goorm.brainsnack.quiz.dto.SimilarQuizResponseDto;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import static goorm.brainsnack.quiz.dto.MemberQuizResponseDto.*;
import static goorm.brainsnack.quiz.dto.QuizRequestDto.*;
import static goorm.brainsnack.quiz.dto.SimilarQuizResponseDto.*;

import java.util.List;

@Getter
@Builder
@Entity
@Table(name = "MemberQuiz_TB")
@AllArgsConstructor(access= AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Slf4j
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "similarQuiz_id")
    private SimilarQuiz similarQuiz;

    private Boolean isCorrect;
    private int choice;

    public static MemberQuizExistSimilarQuizDto getMemberQuizDto(MemberQuiz memberQuiz) {

        return MemberQuizExistSimilarQuizDto.builder()
                .quizNum(memberQuiz.getQuiz().getQuizNum())
                .id(memberQuiz.getQuiz().getId())
                .build();
    }

    public static MemberQuizWithIsCorrectDto getMemberSimilarQuizDto(MemberQuiz memberQuiz , int count) {
        return MemberQuizWithIsCorrectDto.builder()
                .isCorrect(memberQuiz.getIsCorrect())
                .quizNum(count)
                .id(memberQuiz.getSimilarQuiz().getId())
                .build();
    }

    public static MemberSimilarQuizDto getMemberSimilarQuizListDto(Member member, List<MemberQuizWithIsCorrectDto> result, int quizNum) {
        return MemberSimilarQuizDto.builder()
                .memberId(member.getId())
                .entryCode(member.getNickname())
                .createSimilarQuizCount(result.size())
                .quizNum(quizNum)
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

    public static MemberQuiz toSimilarQuiz(SimilarQuizSingleGradeRequestDto request, Member member,SimilarQuiz similarQuiz) {

        boolean userCorrect = false;
        if (similarQuiz.getAnswer() == request.getChoice()) {
            userCorrect = true;
        }
        return MemberQuiz.builder()
                .member(member)
                .similarQuiz(similarQuiz)
                .isCorrect(userCorrect)
                .choice(request.getChoice())
                .build();
    }
}
