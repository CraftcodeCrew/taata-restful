package de.ka.taata.rest.misc;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 */
@ControllerAdvice
public class ExceptionAdvice {

    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handel(Exception ex) {
        return ex.getMessage();
    }

}
