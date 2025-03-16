package az.ingress.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ExceptionMessage {

    UNEXPECTED_EXCEPTION_CODE("UNEXCEPTED_EXCEPTION"),
    UNEXCEPTED_EXCEPTION_MESSAGE("Unexcepted exception occured"),

    CAR_NOT_FOUND_CODE("CAR_NOT_FOUND"),
    CAR_NOT_FOUND_MESSAGE("Car with id:%s not found");

    private final String message;

}
