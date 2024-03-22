package goorm.brainsnack.quiz.domain;

import goorm.brainsnack.global.BaseEntity;
import goorm.brainsnack.member.domain.Member;
import goorm.brainsnack.member.dto.MemberResponseDto;
import goorm.brainsnack.quiz.dto.SimilarQuizResponseDto;
import jakarta.persistence.*;
import lombok.*;

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

    /**
     * 풀었던 유사 문제 조회를 하기 위해서 필요한 필드
     * 유사 문제가 만들어질 때 어떤 문제로부터 만들어졌는지에 대한 정보를 담아둬야 추적이 가능하다.
     */
    private Long basedQuizId;

    public static MemberQuizDto getMemberQuizDto(MemberQuiz memberQuiz) {
        return MemberQuizDto.builder()
                .quizNum(memberQuiz.getQuiz().getQuizNum())
                .quizId(memberQuiz.getQuiz().getId())
                .build();
    }

    public static MemberQuizWithIsCorrectDto getMemberSimilarQuizDto(MemberQuiz memberQuiz , int count) {
        return MemberQuizWithIsCorrectDto.builder()
                .isCorrect(memberQuiz.getIsCorrect())
                .quizNum(count)
                .quizId(memberQuiz.getQuiz().getId())
                .build();
    }

    public static MemberSimilarQuizDto getMemberSimilarQuizListDto(MemberResponseDto.MemberDto member ,
                                                                                          List<MemberQuizWithIsCorrectDto> result, int quizNum) {
        return MemberSimilarQuizDto.builder()
                .memberId(member.getId())
                .entryCode(member.getEntryCode())
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

    public static MemberQuiz toSimilarQuiz(SimilarQuizSingleGradeRequestDto request, Member member, Quiz quiz , Long basedQuizId) {

        boolean userCorrect = false;
        if (quiz.getAnswer() == request.getChoice()) {
            userCorrect = true;
        }
        return MemberQuiz.builder()
                .member(member)
                .quiz(quiz)
                .basedQuizId(basedQuizId)
                .isCorrect(userCorrect)
                .choice(request.getChoice())
                .build();
    }
}
