package goorm.brainsnack.quiz.init;

import goorm.brainsnack.quiz.domain.Quiz;
import goorm.brainsnack.quiz.domain.QuizCategory;
import goorm.brainsnack.quiz.repository.QuizRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.core.ResponseBytes;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

import java.io.*;
import java.util.Arrays;
import java.util.Iterator;

@Component
@RequiredArgsConstructor
public class QuizInit {

    private final InitService initService;
    private final QuizRepository quizRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void init() throws IOException {
        /**
         * DB 에 데이터가 있는 경우에는 초기화를 하지 않습니다.
         */
        if (quizRepository.findAll().size() == 0) {
            initService.init();
        }
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final QuizRepository quizRepository;

        private final S3Client s3Client;
        @Value("${aws.s3.bucket}")
        private String bucketName;
        private String key = "data-init.xlsx";

        public void init() throws IOException {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            ResponseBytes<GetObjectResponse> objectAsBytes = s3Client.getObjectAsBytes(getObjectRequest);
            byte[] excelBytes = objectAsBytes.asByteArray();
            InputStream inputStream = new ByteArrayInputStream(excelBytes);
            Workbook workbook = new XSSFWorkbook(inputStream);

            // 첫 번째 시트 선택
            Sheet sheet = workbook.getSheetAt(0);

            // 헤더 행은 건너뛰기 위해 반복자를 첫 번째 데이터 행으로 이동
            Iterator<Row> rowIterator = sheet.iterator();

            if (rowIterator.hasNext()) {
                rowIterator.next(); // 헤더 행은 건너뜁니다.
            }
            // 예외가 있는 문제 (보기가 숫자로 인식)
            int[] exceptionRowNumbers = {19, 27, 28, 29, 30};

            // 각 행을 순회하며 데이터를 읽어서 객체로 변환하여 리스트에 추가
            while (rowIterator.hasNext()) {
                // 보기가 숫자로 인식되는 문제들은 createExceptionQuiz 로 만들어집니다.
                Row row = rowIterator.next();
                if (Arrays.stream(exceptionRowNumbers).anyMatch(rowNum -> rowNum == row.getRowNum())) {
                    Quiz quiz = createExceptionQuiz(row);
                    quizRepository.save(quiz);
                    continue;
                }
                Quiz quiz = createQuiz(row);
                quizRepository.save(quiz);
            }
            // 리소스 정리
            workbook.close();
            inputStream.close();
        }
    }

    private static Quiz createQuiz(Row row) {
        String category = row.getCell(0).getStringCellValue();
        QuizCategory quizCategory = QuizCategory.LANG;
        if (category.equals("math")) {
            quizCategory = QuizCategory.MATH;
        } else if (category.equals("deduce")) {
            quizCategory = QuizCategory.DEDUCE;
        }
        int quizNum = (int) row.getCell(1).getNumericCellValue();
        String title = row.getCell(2).getStringCellValue();
        String example = row.getCell(3).getStringCellValue();
        String choiceFirst = row.getCell(4).getStringCellValue();
        String choiceSecond = row.getCell(5).getStringCellValue();
        String choiceThird = row.getCell(6).getStringCellValue();
        String choiceFourth = row.getCell(7).getStringCellValue();
        String choiceFifth = row.getCell(8).getStringCellValue();
        int answer = (int) row.getCell(9).getNumericCellValue();
        String solution = row.getCell(10).getStringCellValue();
        return Quiz.of(quizNum, title, example, choiceFirst,
                choiceSecond, choiceThird, choiceFourth, choiceFifth, answer, solution, Boolean.FALSE, quizCategory);
    }

    private static Quiz createExceptionQuiz(Row row) {
        String category = row.getCell(0).getStringCellValue();
        QuizCategory quizCategory = QuizCategory.LANG;
        if (category.equals("math")) {
            quizCategory = QuizCategory.MATH;
        } else if (category.equals("deduce")) {
            quizCategory = QuizCategory.DEDUCE;
        }
        int quizNum = (int) row.getCell(1).getNumericCellValue();
        String title = row.getCell(2).getStringCellValue();
        String example = row.getCell(3).getStringCellValue();
        String choiceFirst = String.valueOf(row.getCell(4).getNumericCellValue());
        String choiceSecond = String.valueOf(row.getCell(5).getNumericCellValue());
        String choiceThird = String.valueOf(row.getCell(6).getNumericCellValue());
        String choiceFourth = String.valueOf(row.getCell(7).getNumericCellValue());
        String choiceFifth = String.valueOf(row.getCell(8).getNumericCellValue());
        int answer = (int) row.getCell(9).getNumericCellValue();
        String solution = row.getCell(10).getStringCellValue();
        return Quiz.of(quizNum, title, example, choiceFirst,
                choiceSecond, choiceThird, choiceFourth, choiceFifth, answer, solution, Boolean.FALSE, quizCategory);

    }
}
