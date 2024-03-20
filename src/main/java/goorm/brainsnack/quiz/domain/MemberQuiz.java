package goorm.brainsnack.quiz.domain;

import goorm.brainsnack.global.BaseEntity;
import goorm.brainsnack.member.domain.Member;
import goorm.brainsnack.member.dto.MemberResponseDto;
import goorm.brainsnack.quiz.dto.MemberQuizResponseDto;
import goorm.brainsnack.quiz.dto.SimilarQuizResponseDto;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

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
}
