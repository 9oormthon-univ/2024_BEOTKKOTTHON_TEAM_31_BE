package goorm.brainsnack.quiz.service;

import goorm.brainsnack.quiz.dto.SimilarQuizRequestDto;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

public interface ChatGPTService {

    List<Map<String, Object>> modelList();

    Map<String, Object> isValidModel(String modelName);

//    Map<String, Object> prompt(SimilarQuizRequestDto.ChatRequestMsgDto chatRequestMsgDto);
}
