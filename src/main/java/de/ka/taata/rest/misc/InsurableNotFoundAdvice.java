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
public class InsurableNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(InsurableNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handel(InsurableNotFoundException ex) {
        return ex.getMessage();
    }

}
