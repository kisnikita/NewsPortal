package myProject.validator;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class NewUserValidationResult {
    @Getter
    private final List<Error> errorList = new ArrayList<>();

    public void addError(Error error){
        errorList.add(error);
    }

    public boolean isValid(){
        return errorList.isEmpty();
    }
}
