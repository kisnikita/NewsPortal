package myProject.exception;

import lombok.Getter;

public class CensorshipException extends RuntimeException{
    @Getter
    private final String VIOLATION = "Вам вынесено предупреждение за нецензурный комментарий\nСледующая попытка карается блокировкой.";
}
