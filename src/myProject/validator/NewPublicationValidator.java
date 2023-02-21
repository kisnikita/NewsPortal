package myProject.validator;

import lombok.SneakyThrows;
import myProject.web.dto.CommentsDto;
import myProject.web.dto.NewsDto;

import java.nio.file.Files;
import java.nio.file.Path;

public class NewPublicationValidator implements ValidatorForPublication<NewPublicationValidationResult> {
    private static final Path ABSOLUTE_PATH = Path.of("C:\\Users\\Пользователь\\IdeaProjects\\http_course_dmdev\\resources\\нецензурные слова.txt");
    private static final NewPublicationValidator INSTANCE = new NewPublicationValidator();

    @SneakyThrows
    @Override
    public NewPublicationValidationResult isValidComment(CommentsDto commentsDto) {
        NewPublicationValidationResult newPublicationValidationResult = new NewPublicationValidationResult();
        for (String line : Files.readAllLines(ABSOLUTE_PATH)) {
            if(commentsDto.getText().contains(line)){
                newPublicationValidationResult.setViolation(true);
            }
        }
        return newPublicationValidationResult;
    }
    @SneakyThrows
    @Override
    public NewPublicationValidationResult isValidNews(NewsDto newsDto) {
        NewPublicationValidationResult newPublicationValidationResult = new NewPublicationValidationResult();
        for (String line : Files.readAllLines(ABSOLUTE_PATH)) {
            if(newsDto.getText().contains(line)){
                newPublicationValidationResult.setViolation(true);
            }
        }
        return newPublicationValidationResult;
    }

    public static NewPublicationValidator getInstance(){
        return INSTANCE;
    }
}
