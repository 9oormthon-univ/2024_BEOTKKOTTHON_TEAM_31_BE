package goorm.brainsnack.quiz.domain;

import goorm.brainsnack.global.BaseEntity;
import goorm.brainsnack.member.domain.Member;
import goorm.brainsnack.member.dto.MemberResponseDto;
import goorm.brainsnack.quiz.dto.SimilarQuizResponseDto;
import jakarta.persistence.*;
import lombok.*;

import static goorm.brainsnack.quiz.dto.MemberQuizResponseDto.*;
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

    @OneToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne
    @JoinColumn(name = "quiz_id")
    private Quiz quiz;
    private Boolean isCorrect;
    private int choice;

    /**
     * 풀었던 유사 문제 조회를 위한 id
     * 어떤 문제로부터 만들어진 문제인지 알아야한다.
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

    public static SimilarQuizResponseDto.MemberSimilarQuizDto getMemberSimilarQuizListDto(MemberResponseDto.MemberDto member ,
                                                                                          List<MemberQuizWithIsCorrectDto> result, int quizNum) {
        return SimilarQuizResponseDto.MemberSimilarQuizDto.builder()
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

    public static MemberQuiz toSimilarQuiz(SimilarQuizSingleGradeRequestDto request, Member member, Quiz quiz) {

        boolean userCorrect = false;
        if (quiz.getAnswer() == request.getChoice()) {
            userCorrect = true;
        }
        return MemberQuiz.builder()
                .member(member)
                .quiz(quiz)
                .basedQuizId(quiz.getId())
                .isCorrect(userCorrect)
                .choice(request.getChoice())
                .build();
    }
}
