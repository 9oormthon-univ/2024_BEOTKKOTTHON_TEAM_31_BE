package goorm.brainsnack.quiz.service;


import goorm.brainsnack.exception.BaseException;
import goorm.brainsnack.exception.ErrorCode;
import goorm.brainsnack.exception.QuizException;
import goorm.brainsnack.member.domain.Member;
import goorm.brainsnack.member.dto.MemberResponseDto;
import goorm.brainsnack.member.repository.MemberRepository;
import goorm.brainsnack.quiz.domain.*;
import goorm.brainsnack.quiz.dto.QuizRequestDto;
import goorm.brainsnack.quiz.dto.SimilarQuizResponseDto;
import goorm.brainsnack.quiz.repository.MemberQuizRepository;
import goorm.brainsnack.quiz.repository.QuizDataRepository;
import goorm.brainsnack.quiz.repository.QuizRepository;
import goorm.brainsnack.quiz.repository.SimilarQuizRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    private final SimilarQuizRepository similarQuizRepository;

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
    public CategoryQuizListDto getCategoryQuizzes(Long memberId, String categoryName) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_USER));
        QuizCategory category = QuizCategory.getInstance(categoryName);
        List<MemberQuiz> memberQuiz = memberQuizRepository.findAllByMember(member);
        List<Quiz> quizzes = quizRepository.findAllByCategory(category);

        List<Quiz> userRemainQuizzes = quizzes.stream()
                .filter(q -> memberQuiz.stream()
                        .noneMatch(mq -> Objects.equals(q.getId(), mq.getQuiz().getId())))
                .toList();

        return CategoryQuizListDto.from(userRemainQuizzes);
    }

    @Transactional
    @Override
    public MultiResultResponseDto gradeMultiQuiz(Long memberId, String categoryInput, MultiGradeRequestDto request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_USER));
        QuizCategory category = QuizCategory.getInstance(categoryInput);

        int totalQuizCounts = quizRepository.findAllByCategory(category).size();
        int wrongQuizCounts = memberQuizRepository.findAllByMemberAndCategoryAndIsCorrect(member, false, category).size();

        List<SingleResultResponseDto> results = new ArrayList<>();
        for (SingleGradeRequestDto gradeRequest : request.getGradeRequests()) {
            // 카테고리 입력 검증
            //    if (!gradeRequest.getCategory().equals(category)) {
//                throw new BaseException(ErrorCode.CATEGORY_CONFLICT);
//            }
            Quiz quiz = quizRepository.findById(gradeRequest.getId())
                    .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_QUIZ));
            // 이미 풀이한 문제는 결과 가져오기
            Optional<MemberQuiz> memberQuiz = memberQuizRepository.findByMemberAndQuiz(member, quiz);
            log.info("mq-{}: {}", gradeRequest.getId(), memberQuiz.isPresent());
            if (memberQuiz.isPresent()) {
                results.add(SingleResultResponseDto.from(memberQuiz.get()));
            } else {
                results.add(SingleResultResponseDto.from(gradeSingleQuiz(memberId, gradeRequest.getId(), gradeRequest)));
            }
        }
        return MultiResultResponseDto.of(totalQuizCounts, wrongQuizCounts, results, category);
    }

    @Override
    @Transactional
    public SimilarQuizSingleGradeDto gradeSingleSimilarQuiz(Long memberId, Long quizId , QuizRequestDto.SimilarQuizSingleGradeRequestDto request) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_USER));

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_QUIZ));

        if (quiz.getCategory() != QuizCategory.getInstance(request.getCategory())) {
            throw new BaseException(ErrorCode.CATEGORY_CONFLICT);
        }

        /**
         * quizNum 을 지정해주기 위해서 가져오는 코드
         */
        List<Quiz> quizList = quizRepository.findAllByCategory(quiz.getCategory());

        SimilarQuiz similarQuiz = SimilarQuiz.of(quiz,(quizList.size() + 1), request.getTitle(), request.getExample(),
                request.getChoiceFirst(), request.getChoiceSecond(), request.getChoiceThird(),
                request.getChoiceFourth(), request.getChoiceFifth(), request.getAnswer(), request.getSolution(),
                quiz.getCategory());

        similarQuizRepository.save(similarQuiz);

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

        return memberQuizRepository
                .findMemberQuizList(member.getId(), quizCategory).stream()
                .filter(memberQuiz -> !memberQuiz.getIsCorrect())
                .map(MemberQuiz::getMemberQuizDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<MemberQuizDto> getCorrectQuizList(Long memberId, String category) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_USER));
        QuizCategory quizCategory = QuizCategory.getInstance(category);

        return memberQuizRepository
                .findMemberQuizList(member.getId(), quizCategory).stream()
                .filter(MemberQuiz::getIsCorrect)
                .map(MemberQuiz::getMemberQuizDto)
                .collect(Collectors.toList());
    }

    @Override
    public SimilarQuizResponseDto.MemberSimilarQuizDto getSimilarQuiz(Long memberId, String category, Long quizId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_USER));

        QuizCategory quizCategory = QuizCategory.getInstance(category);

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_QUIZ));

        // 해당 유저가 푼 기존 문제에서 만들고 풀었던 유사 문제 조회
        List<MemberQuiz> memberSimilarQuizList = memberQuizRepository.findMemberSimilarQuiz(member.getId(), quizCategory,quizId);

        AtomicInteger count = new AtomicInteger(1);
        List<MemberQuizWithIsCorrectDto> memberQuizList = memberSimilarQuizList
                .stream().map(memberQuiz -> getMemberSimilarQuizDto(memberQuiz, count.getAndIncrement()))
                .toList();
        return getMemberSimilarQuizListDto(member,memberQuizList,quiz.getQuizNum());
    }

    @Override
    public SingleGradeDto getSingleResult(Long memberId, Long quizId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_USER));
        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_EXIST_QUIZ));

        MemberQuiz memberQuiz = memberQuizRepository.findByMemberAndQuiz(member, quiz)
                .orElseThrow(() -> new BaseException(ErrorCode.UNSOLVED_QUIZ));
        QuizData data = dataRepository.findByQuiz(quiz)
                .orElseThrow(() -> new BaseException(ErrorCode.NOT_FOUND));

        return SingleGradeDto.of(quiz, memberQuiz, data, getRatio(data));
    }

    private int getRatio(QuizData data) {
        int ratio = 0;
        if (data.getQuizParticipantsCounts() != 0) {
            ratio = data.getCorrectAnswerCounts() / data.getQuizParticipantsCounts() * 100;
        }
        return ratio;
    }

}
