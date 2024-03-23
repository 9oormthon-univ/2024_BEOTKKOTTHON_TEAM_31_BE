package goorm.brainsnack.quiz.service;


import goorm.brainsnack.exception.BaseException;
import goorm.brainsnack.exception.ErrorCode;
import goorm.brainsnack.exception.QuizException;
import goorm.brainsnack.member.domain.Member;
import goorm.brainsnack.member.dto.MemberResponseDto;
import goorm.brainsnack.member.repository.MemberRepository;
import goorm.brainsnack.quiz.domain.MemberQuiz;
import goorm.brainsnack.quiz.domain.Quiz;
import goorm.brainsnack.quiz.domain.QuizCategory;
import goorm.brainsnack.quiz.domain.QuizData;
import goorm.brainsnack.quiz.dto.QuizRequestDto;
import goorm.brainsnack.quiz.dto.SimilarQuizResponseDto;
import goorm.brainsnack.quiz.repository.MemberQuizRepository;
import goorm.brainsnack.quiz.repository.QuizDataRepository;
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

import static goorm.brainsnack.quiz.domain.MemberQuiz.getMemberSimilarQuizDto;
import static goorm.brainsnack.quiz.domain.MemberQuiz.getMemberSimilarQuizListDto;
import static goorm.brainsnack.quiz.dto.MemberQuizResponseDto.MemberQuizDto;
import static goorm.brainsnack.quiz.dto.MemberQuizResponseDto.MemberQuizWithIsCorrectDto;
import static goorm.brainsnack.quiz.dto.QuizRequestDto.MultiGradeRequestDto;
import static goorm.brainsnack.quiz.dto.QuizRequestDto.SingleGradeRequestDto;
import static goorm.brainsnack.quiz.dto.QuizResponseDto.*;
import static goorm.brainsnack.quiz.dto.QuizResponseDto.SimilarQuizSingleGradeDto.of;

@Slf4j
@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final MemberRepository memberRepository;
    private final MemberQuizRepository memberQuizRepository;
    private final QuizRepository quizRepository;
    private final QuizDataRepository dataRepository;

    @Override
    public QuizDetailDto findQuiz(Long quizId) {
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new QuizException(ErrorCode.NOT_EXIST_QUIZ));
        return QuizDetailDto.from(quiz);
    }
    
    @Override
    public GetTotalMemberDto getTotalNum(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_USER));
        List<MemberQuiz> members = memberQuizRepository.findAllByMember(member);

        // 풀었던 퀴즈가 없는 경우
        if (members.isEmpty()) {
            return GetTotalMemberDto.from(0);
        }

        int totalQuizNum = memberQuizRepository.findAllByMember(member).size();
        return GetTotalMemberDto.from(totalQuizNum);
    }

    @Override
    public CategoryQuizListDto getCategoryQuizzes(String categoryName) {
        QuizCategory category = QuizCategory.getInstance(categoryName);

        List<Quiz> quizzes = quizRepository.findAllByCategory(category);

        return CategoryQuizListDto.builder()
                .size(quizzes.size())
                .quizzes(quizzes.stream()
                        .map(SingleQuizDto::from)
                        .toList())
                .build();
    }

    @Transactional
    @Override
    public MultiResultResponseDto gradeMultiQuiz(Long memberId, String categoryInput, MultiGradeRequestDto request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_USER));
        List<MemberQuiz> memberQuizzes = memberQuizRepository.findAllByMemberAndCategory(member, QuizCategory.getInstance(categoryInput));
        QuizCategory category = QuizCategory.getInstance(categoryInput);

        int totalQuizCounts = quizRepository.findAllByCategory(category).size();
        int wrongQuizCounts = memberQuizRepository.findAllByMemberAndCategoryAndIsCorrect(member, false, category).size();

        List<SingleGradeDto> results = new ArrayList<>();
        for (SingleGradeRequestDto gradeRequest : request.getGradeRequests()) {
//            if (!gradeRequest.getCategory().equals(category)) {
//                throw new BaseException(ErrorCode.CATEGORY_CONFLICT);
//            }
            results.add(gradeSingleQuiz(memberId, gradeRequest.getId(), gradeRequest));
        }
        return MultiResultResponseDto.of(totalQuizCounts, wrongQuizCounts, memberQuizzes, category);
    }

    @Override
    @Transactional
    public SimilarQuizSingleGradeDto gradeSingleSimilarQuiz(Long memberId, Long quizId , QuizRequestDto.SimilarQuizSingleGradeRequestDto request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_USER));

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_QUIZ));

        /**
         * quizNum 을 지정해주기 위해서 가져오는 코드
         */
        List<Quiz> quizList = quizRepository.findAllByCategory(quiz.getCategory());

        Quiz similarQuiz = Quiz.of((quizList.size()+1), request.getTitle(), request.getExample(),
                request.getChoiceFirst(), request.getChoiceSecond(), request.getChoiceThird(),
                request.getChoiceFourth(), request.getChoiceFifth(), request.getAnswer(), request.getSolution(),
                quiz.getCategory());
        quizRepository.save(similarQuiz);

        MemberQuiz memberQuiz = memberQuizRepository.save(MemberQuiz.toSimilarQuiz(request, member, similarQuiz , quiz.getId()));

        return of(similarQuiz,memberQuiz);
    }

    @Transactional
    @Override
    public SingleGradeDto gradeSingleQuiz(Long memberId, Long quizId, SingleGradeRequestDto request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_USER));
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_QUIZ));

        if (quiz.getCategory() != QuizCategory.getInstance(request.getCategory())) {
            throw new BaseException(ErrorCode.CATEGORY_CONFLICT);
        }

        Optional<MemberQuiz> optionalMemberQuiz = memberQuizRepository.findByMemberAndQuiz(member, quiz);
        Optional<QuizData> optionalQuizData = dataRepository.findByQuiz(quiz);

        if (optionalMemberQuiz.isPresent()) {
            throw new BaseException(ErrorCode.ALREADY_FINISH_QUIZ);
        }

        MemberQuiz memberQuiz = memberQuizRepository.save(MemberQuiz.of(request, member, quiz));
        QuizData data = optionalQuizData
                .orElseGet(() -> dataRepository.save(QuizData.from(quiz)));

        data.updateQuizData(memberQuiz);

        return SingleGradeDto.of(quiz, memberQuiz, data, getRatio(data));
    }

    @Override
    public List<MemberQuizDto> getWrongQuizList(Long memberId, String category) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_USER));

        QuizCategory quizCategory = QuizCategory.getInstance(category);

        List<MemberQuizDto> result = memberQuizRepository
                .findMemberQuizList(member.getId(), quizCategory).stream()
                .filter(memberQuiz -> !memberQuiz.getIsCorrect())
                .map(MemberQuiz::getMemberQuizDto)
                .collect(Collectors.toList());
        return result;
    }

    @Override
    public List<MemberQuizDto> getCorrectQuizList(Long memberId, String category) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_USER));
        QuizCategory quizCategory = QuizCategory.getInstance(category);

        List<MemberQuizDto> result = memberQuizRepository
                .findMemberQuizList(member.getId(), quizCategory).stream()
                .filter(MemberQuiz::getIsCorrect)
                .map(MemberQuiz::getMemberQuizDto)
                .collect(Collectors.toList());
        return result;
    }

    @Override
    public SimilarQuizResponseDto.MemberSimilarQuizDto getSimilarQuiz(Long memberId, String category, Long quizId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_USER));

        MemberResponseDto.MemberDto findMember = Member.toMemberDto(member);

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
    private int getRatio(QuizData data) {
        int ratio = 0;
        if (data.getQuizParticipantsCounts() != 0) {
            ratio = data.getCorrectAnswerCounts() / data.getQuizParticipantsCounts() * 100;
        }
        return ratio;
    }

}
