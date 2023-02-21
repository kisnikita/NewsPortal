package myProject.web.dao;

import lombok.SneakyThrows;
import myProject.web.model.Gender;
import myProject.web.model.Role;
import myProject.web.model.User;
import myProject.web.util.ConnectionManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
//добавить страну и проверку при регистрации на то, что пользователь не может зарегистрироваться если ему меньше 18 лет
public class UserDao implements Dao<Integer, User> {
    private static final UserDao INSTANCE = new UserDao();
    private static final String FIND_BY_EMAIL_AND_PASSWORD = """
            select * from news_repository.news_schema.users where email = ? and password = ? 
            """;
    private static final String SAVE_USER = """
            insert into news_repository.news_schema.users (name, birthday, country, email, password, role, gender,isbanned,notice)
             VALUES (?,?,?,?,?,?,?,?,?)
            """;
    private static final String FIND_USER_BY_ID = """
            select * from news_repository.news_schema.users where id = ?
            """;

    @SneakyThrows
    public Optional<User> findByEmailAndPassword(String email,String password){
        try (Connection connection = ConnectionManager.getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_BY_EMAIL_AND_PASSWORD)) {
             preparedStatement.setObject(1,email);
             preparedStatement.setObject(2,password);
             ResultSet resultSet = preparedStatement.executeQuery();
             if(resultSet.next()){
                 return Optional.of(buildEntity(resultSet));
             }
            return Optional.empty();
        }
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public boolean update(String s) {
        return false;
    }

    @SneakyThrows
    @Override
    public Optional<User> findById(Integer id) {
        try (Connection connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(FIND_USER_BY_ID)) {
            preparedStatement.setObject(1,id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                return Optional.of(buildEntity(resultSet));
            }
            return Optional.empty();
        }
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    @SneakyThrows
    @Override
    public User create(User entity) {
        try (Connection connection = ConnectionManager.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(SAVE_USER, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setObject(1,entity.getName());
            preparedStatement.setObject(2,entity.getBirthday());
            preparedStatement.setObject(3,entity.getCountry());
            preparedStatement.setObject(4,entity.getEmail());
            preparedStatement.setObject(5,entity.getPassword());
            preparedStatement.setObject(6,entity.getRole().name());
            preparedStatement.setObject(7,entity.getGender().name());
            preparedStatement.setObject(8,entity.getIsBanned());
            preparedStatement.setObject(9,entity.getNotice());
            preparedStatement.executeUpdate();
            return entity;
        }
    }

    public static UserDao getInstance(){
        return INSTANCE;
    }

    @SneakyThrows
    private User buildEntity(ResultSet resultSet){
        return User.builder()
                .id(resultSet.getObject("id",Integer.class))
                .name(resultSet.getObject("name",String.class))
                .birthday(resultSet.getObject("birthday", LocalDate.class))
                .isBanned(resultSet.getObject("isbanned",Boolean.class))
                .banDate(resultSet.getObject("ban_date",LocalDate.class))
                .notice(resultSet.getObject("notice",Boolean.class))
                .country(resultSet.getObject("country",String.class))
                .email(resultSet.getObject("email",String.class))
                .password(resultSet.getObject("password",String.class))
                .role(Role.valueOf(resultSet.getObject("role", String.class)))
                .gender(Gender.valueOf(resultSet.getObject("gender",String.class)))
                .build();
    }
}
