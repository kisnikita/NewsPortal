package myProject.validator;

import lombok.Value;

@Value(staticConstructor = "of")
public class Error {
    String text;
}
