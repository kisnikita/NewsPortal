package myProject.validator;

import myProject.web.dto.CommentsDto;
import myProject.web.dto.NewsDto;
import myProject.web.model.Comments;

public interface ValidatorForPublication<R>{
    R isValidComment(CommentsDto commentsDto);

    R isValidNews(NewsDto newsDto);
}
