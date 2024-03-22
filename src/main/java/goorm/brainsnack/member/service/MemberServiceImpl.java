package goorm.brainsnack.member.service;

import goorm.brainsnack.exception.BaseException;
import goorm.brainsnack.exception.ErrorCode;
import goorm.brainsnack.member.domain.Member;
import goorm.brainsnack.member.dto.MemberResponseDto;
import goorm.brainsnack.member.repository.MemberRepository;
import goorm.brainsnack.quiz.domain.MemberQuiz;
import goorm.brainsnack.quiz.domain.Quiz;
import goorm.brainsnack.quiz.domain.QuizCategory;
import goorm.brainsnack.quiz.dto.MemberQuizResponseDto;
import goorm.brainsnack.quiz.dto.QuizResponseDto;
import goorm.brainsnack.quiz.dto.SimilarQuizResponseDto;
import goorm.brainsnack.quiz.repository.MemberQuizRepository;
import goorm.brainsnack.quiz.repository.QuizRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static goorm.brainsnack.member.dto.MemberResponseDto.*;
import static goorm.brainsnack.quiz.domain.MemberQuiz.*;
import static goorm.brainsnack.quiz.dto.MemberQuizResponseDto.*;
import static goorm.brainsnack.quiz.dto.SimilarQuizResponseDto.*;
import static java.util.stream.Collectors.reducing;
import static java.util.stream.Collectors.toList;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final QuizRepository quizRepository;
    private final MemberQuizRepository memberQuizRepository;
    @Transactional
    @Override
    public LoginDto login(String temporaryId) {
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
    public MemberDto findById(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_USER));
        return Member.toMemberDto(member);
    }


    // 내가 틀린 문제 (기존)
    @Override
    public List<MemberQuizDto> getWrongQuizList(Long memberId , String category) {
        MemberDto member = findById(memberId);
        QuizCategory quizCategory = QuizCategory.getInstance(category);

        List<MemberQuizDto> result = memberQuizRepository
                .findMemberQuizList(member.getId(), quizCategory).stream()
                .filter(memberQuiz -> !memberQuiz.getIsCorrect())
                .map(MemberQuiz::getMemberQuizDto)
                .collect(Collectors.toList());
        return result;
    }

    // 내가 맞은 문제 (기존)
    @Override
    public List<MemberQuizDto> getCorrectQuizList(Long memberId , String category) {
        MemberDto member = findById(memberId);
        QuizCategory quizCategory = QuizCategory.getInstance(category);

        List<MemberQuizDto> result = memberQuizRepository
                .findMemberQuizList(member.getId(), quizCategory).stream()
                .filter(MemberQuiz::getIsCorrect)
                .map(MemberQuiz::getMemberQuizDto)
                .collect(Collectors.toList());
        return result;
    }

    @Override
    public MemberSimilarQuizDto getSimilarQuiz(Long memberId, String category , Long quizId) {
        MemberDto findMember = findById(memberId);

        QuizCategory quizCategory = QuizCategory.getInstance(category);

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_QUIZ));

        List<MemberQuiz> memberSimilarQuizList = memberQuizRepository.findMemberSimilarQuiz(findMember.getId(), quizCategory,quizId);
        AtomicInteger count = new AtomicInteger(1);

        List<MemberQuizWithIsCorrectDto> memberQuizList = memberSimilarQuizList
                .stream().map(memberQuiz -> getMemberSimilarQuizDto(memberQuiz, count.getAndIncrement()))
                .toList();
        return getMemberSimilarQuizListDto(findMember,memberQuizList,quiz.getQuizNum());
    }
}
