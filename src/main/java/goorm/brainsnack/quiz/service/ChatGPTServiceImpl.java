package goorm.brainsnack.quiz.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import goorm.brainsnack.exception.BaseException;
import goorm.brainsnack.exception.ErrorCode;
import goorm.brainsnack.global.config.ChatGPTConfig;
import goorm.brainsnack.quiz.dto.ChatGPTRequestDto;
import goorm.brainsnack.quiz.dto.QuizResponseDto;
import goorm.brainsnack.quiz.dto.SimilarQuizResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service @Slf4j
@RequiredArgsConstructor
public class ChatGPTServiceImpl implements ChatGPTService {

    private final ChatGPTConfig chatGPTConfig;

    @Value("${openai.url.prompt}")
    private String promptUrl;
    @Override
    public SimilarQuizResponseDto.CreateDto prompt(ChatGPTRequestDto.ChatCompletionDto chatCompletionDto, QuizResponseDto.QuizDetailDto quizDetailDto) {

        Map<String, Object> resultMap = new HashMap<>();
        // 토큰 정보가 포함된 Header 가져오기
        HttpHeaders headers = chatGPTConfig.httpHeaders();

        // 통신을 위한 RestTemplate 구성
        HttpEntity<ChatGPTRequestDto.ChatCompletionDto> requestEntity = new HttpEntity<>(chatCompletionDto, headers);

        ResponseEntity<String> response = chatGPTConfig
                .restTemplate()
                .exchange(promptUrl, HttpMethod.POST, requestEntity, String.class);

        try {
            ObjectMapper om = new ObjectMapper();
            resultMap = om.readValue(response.getBody(), new TypeReference<>() {
            });
        } catch (JsonMappingException e) {
            log.debug("JsonMappingException :: " + e.getMessage());
        } catch (JsonProcessingException e) {
            log.debug("RuntimeException :: " + e.getMessage());
        }

        // GPT 로부터 넘어온 응답값 중에서 만들어준 유사 문제만 추출
        String[] split = extractGPTMessage(resultMap);

        SimilarQuizResponseDto.CreateDto similarQuiz = createSimilarQuiz(quizDetailDto, split);

        // similarQuiz 가 제대로 생성됐는지 확인하는 로직
        if (SimilarQuizFieldCheck(similarQuiz)) {
            throw new BaseException(ErrorCode.CREATE_QUIZ_BAD_REQUEST);
        }

        return similarQuiz;
    }


    /**
     * 문제가 정상적으로 동작하는지 확인하는 로직을 보완해야할 필요가 있을 것 같음.
     */
    private static boolean SimilarQuizFieldCheck(SimilarQuizResponseDto.CreateDto similarQuiz) {
        // example , 5번이 없는 경우는 일단 보류
        boolean hasEmptyField =
                        similarQuiz.getTitle().equals("X") || similarQuiz.getTitle().isEmpty() ||
                        similarQuiz.getChoiceFirst().equals("X")  || similarQuiz.getChoiceSecond().equals("X") ||
                        similarQuiz.getChoiceThird().equals("X")  || similarQuiz.getChoiceFourth().equals("X") ||
                        similarQuiz.getSolution().equals("X");
        return hasEmptyField;
    }

    private static String[] extractGPTMessage(Map<String, Object> resultMap) {
        ArrayList<Object> choices = (ArrayList<Object>) resultMap.get("choices");
        LinkedHashMap<String, Object> choice = (LinkedHashMap<String, Object>) choices.get(0);
        LinkedHashMap<String, Object> message = (LinkedHashMap<String, Object>) choice.get("message");
        String content = (String) message.get("content");
        String[] split = content.split("\n");
        return split;
    }
    private static SimilarQuizResponseDto.CreateDto createSimilarQuiz(QuizResponseDto.QuizDetailDto quizDetailDto, String[] split) {
        String title = "X";
        String choiceFirst = "X";
        String choiceSecond = "X";
        String choiceThird = "X";
        String choiceFourth = "X";
        String choiceFifth = "X";
        int answer = 0;
        String solution = "X";
        String example = "X";

        for (String s : split) {
            String[] div = s.split(":");
            // GPT 가 공백을 몇 개 더 만들어서 생성하는 경우는 continue
            if (div.length == 1) {
                continue;
            }
            String key = div[0].trim();
            String value = div[1].trim();

            if (key.equals("문제")) {
                title = value;
            }
            else if (key.equals("예시")) {
                example = value;
            }
            else if(key.contains("1") || key.contains("1번")) {
                choiceFirst = value;
            } else if(key.contains("2") || key.contains("2번")) {
                choiceSecond = value;
            } else if(key.contains("3") || key.contains("3번")) {
                choiceThird = value;
            } else if(key.contains("4") || key.contains("4번")) {
                choiceFourth = value;
            } else if(key.contains("5") || key.contains("5번")) {
                choiceFifth = value;
            } else if(key.equals("정답")) {
                // 정답이 1번 , 2번 처럼 나오는 경우 예외 처리
                answer = Integer.parseInt(value.replaceAll("[^0-9]", ""));
            } else if(key.equals("해설")) {
                solution = value;
            }
        }

        return SimilarQuizResponseDto.CreateDto.builder()
                .title(title)
                .solution(solution)
                .choiceFirst(choiceFirst)
                .choiceSecond(choiceSecond)
                .choiceThird(choiceThird)
                .choiceFourth(choiceFourth)
                .choiceFifth(choiceFifth)
                .answer(answer)
                .example(example)
                .quizNum(quizDetailDto.getQuizNum())
                .category(quizDetailDto.getCategory())
                .isSimilar(Boolean.TRUE)
                .build();
    }
}

