package goorm.brainsnack.member.service;

import goorm.brainsnack.exception.BaseException;
import goorm.brainsnack.exception.ErrorCode;
import goorm.brainsnack.member.domain.Member;
import goorm.brainsnack.member.dto.MemberResponseDto;
import goorm.brainsnack.member.repository.MemberRepository;
import goorm.brainsnack.quiz.domain.MemberQuiz;
import goorm.brainsnack.quiz.domain.QuizCategory;
import goorm.brainsnack.quiz.dto.MemberQuizResponseDto;
import goorm.brainsnack.quiz.dto.QuizResponseDto;
import goorm.brainsnack.quiz.dto.SimilarQuizResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.reducing;
import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    @Override
    public MemberResponseDto.LoginDto login(String temporaryId) {
        Optional<Member> optionalMember = memberRepository.findByTemporaryId(temporaryId);

        //기존 멤버 확인
        if (optionalMember.isPresent()) {
            return Member.toMemberRequestDto(optionalMember.get());
        }

        Member member = Member.from(temporaryId);
        memberRepository.save(member);

        return Member.toMemberRequestDto(member);
    }

    @Override
    public MemberResponseDto.MemberDto findById(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_USER));
        return Member.toMemberDto(member);
    }


    // 내가 틀린 문제 (기존)
    @Override
    public List<MemberQuizResponseDto.MemberQuizDto> getWrongQuizList(Long memberId , String category) {
        MemberResponseDto.MemberDto member = findById(memberId);
        QuizCategory quizCategory = QuizCategory.getInstance(category);

        List<MemberQuizResponseDto.MemberQuizDto> result = memberRepository
                .findMemberQuizList(member.getId(), quizCategory).stream()
                .filter(memberQuiz -> !memberQuiz.getIsCorrect())
                .map(MemberQuiz::getMemberQuizDto)
                .collect(Collectors.toList());
        return result;
    }

    // 내가 맞은 문제 (기존)
    @Override
    public List<MemberQuizResponseDto.MemberQuizDto> getCorrectQuizList(Long memberId , String category) {
        MemberResponseDto.MemberDto member = findById(memberId);
        QuizCategory quizCategory = QuizCategory.getInstance(category);

        List<MemberQuizResponseDto.MemberQuizDto> result = memberRepository
                .findMemberQuizList(member.getId(), quizCategory).stream()
                .filter(MemberQuiz::getIsCorrect)
                .map(MemberQuiz::getMemberQuizDto)
                .collect(Collectors.toList());
        return result;
    }

    @Override
    public SimilarQuizResponseDto.MemberSimilarQuizDto getSimilarQuiz(Long memberId, Long quizId , String category) {
        MemberResponseDto.MemberDto findMember = findById(memberId);
        QuizCategory quizCategory = QuizCategory.getInstance(category);

        List<MemberQuiz> memberSimilarQuizList = memberRepository.findMemberSimilarQuiz(memberId, quizId,quizCategory);

        AtomicInteger count = new AtomicInteger(1);
        List<MemberQuizResponseDto.MemberQuizDto> result = memberSimilarQuizList.stream()
                .map(memberQuiz -> MemberQuiz.getMemberSimilarQuizDto(memberQuiz , count.getAndIncrement()))
                .collect(toList());

        return MemberQuiz.getMemberSimilarQuizListDto(findMember ,result);
    }
}
