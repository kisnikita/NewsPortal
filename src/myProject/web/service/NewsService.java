package myProject.web.service;

import myProject.exception.BanException;
import myProject.exception.CensorshipException;
import myProject.mapper.NewsDtoMapper;
import myProject.validator.NewPublicationValidationResult;
import myProject.validator.NewPublicationValidator;
import myProject.web.dao.NewsDao;
import myProject.web.dao.UserDao;
import myProject.web.dto.CommentsDto;
import myProject.web.dto.NewsDto;
import myProject.web.model.News;
import myProject.web.model.User;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public class NewsService {
    private final NewsDao newsDao = NewsDao.getInstance();
    private final NewsDtoMapper newsDtoMapper = NewsDtoMapper.getInstance();
    private final NewPublicationValidator newPublicationValidator= NewPublicationValidator.getInstance();
    private final UserDao userDao = UserDao.getInstance();
    private static final NewsService INSTANCE = new NewsService();

    public List<NewsDto> findAllNews(){
        List<News> newsList = newsDao.findAll();
        return newsList.stream().map(news ->
                NewsDto.builder()
                        .name(news.getName())
                        .text(news.getText())
                        .id(news.getId())
                        .build()).toList();
    }

    public static NewsService getInstance(){
        return INSTANCE;
    }

    public void create(NewsDto newsDto) {
        //валидация не нужна
        //просто мапим и все
        if(!checkUser(newsDto)){
            throw new BanException();
        }
        NewPublicationValidationResult newNewsValidationResult = newPublicationValidator.isValidNews(newsDto);
        if (newNewsValidationResult.isValid()) {
            invalidAction(newsDto);
            throw new CensorshipException();
        }
        News news = newsDtoMapper.mapToEntity(newsDto);
        newsDao.create(news);
    }

    public Optional<NewsDto> findById(String id){
        return newsDao.findById(id).map(news -> NewsDto.builder()
                .id(news.getId())
                .name(news.getName())
                .text(news.getText())
                .build());
    }

    private boolean checkUser(NewsDto newsDto){
        Optional<User> optionalUser = userDao.findById(newsDto.getAuthorId());
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            return !user.getIsBanned();
        }
        return true;
    }

    private void invalidAction(NewsDto newsDto) {
        Optional<User> userOptional = userDao.findById(newsDto.getAuthorId());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setNotice(true);
            if (user.getNotice()) {
                user.setIsBanned(true);
                user.setBanDate(LocalDate.now());
            }
        }
    }

    public boolean update(String newName,String newContent,String id){
        if(newName != null && newContent != null){
           return newsDao.updateAll(newName,newContent,id);
        } else if (newName == null) {
           return newsDao.updateContent(newContent,id);
        }
        else {
           return newsDao.updateName(newName,id);
        }
    }
}
