package goorm.brainsnack.quiz.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import goorm.brainsnack.global.config.ChatGPTConfig;
import goorm.brainsnack.quiz.domain.QuizCategory;
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
    public SimilarQuizResponseDto.CreateDto prompt(ChatGPTRequestDto.ChatCompletionDto chatCompletionDto, QuizResponseDto.QuizDetailDto quizDto) {
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

        for (String s : split) {
            System.out.println(s);
            System.out.println("***");
        }

        SimilarQuizResponseDto.CreateDto similarQuiz = createSimilarQuiz(quizDto, split);
        return similarQuiz;
    }


    private static String[] extractGPTMessage(Map<String, Object> resultMap) {
        ArrayList<Object> choices = (ArrayList<Object>) resultMap.get("choices");
        LinkedHashMap<String, Object> choice = (LinkedHashMap<String, Object>) choices.get(0);
        LinkedHashMap<String, Object> message = (LinkedHashMap<String, Object>) choice.get("message");
        String content = (String) message.get("content");
        String[] split = content.split("\n");
        return split;
    }


    private static SimilarQuizResponseDto.CreateDto createSimilarQuiz(QuizResponseDto.QuizDetailDto quizDto , String[] split) {
        String title = null;
        String choiceFirst = null;
        String choiceSecond = null;
        String choiceThird = null;
        String choiceFourth = null;
        String choiceFifth = null;
        int answer = 0;
        String solution = null;
        String example = null;

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
            else if(key.contains("1")) {
                choiceFirst = value;
            } else if(key.contains("2")) {
                choiceSecond = value;
            } else if(key.contains("3")) {
                choiceThird = value;
            } else if(key.contains("4")) {
                choiceFourth = value;
            } else if(key.contains("5")) {
                choiceFifth = value;
            } else if(key.equals("정답")) {
                answer = Integer.parseInt(value);
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
                .quizNum(quizDto.getQuizNum())
                .category(quizDto.getCategory())
                .isSimilar(Boolean.TRUE)
                .build();
    }
}

