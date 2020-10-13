package com.jiangwensi.mrbs;

import com.jiangwensi.mrbs.constant.MyResponseStatus;
import com.jiangwensi.mrbs.exception.*;
import com.jiangwensi.mrbs.model.response.GeneralResponse;
import com.jiangwensi.mrbs.utils.MyStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.UUID;

/**
 * Created by Jiang Wensi on 16/8/2020
 */
@Slf4j
@RestControllerAdvice
public class AdviceExceptionHandler {

    @ExceptionHandler(value = {NotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public GeneralResponse handleNotFoundException(Exception e) {
        return generalErrorResponse(e, false);
    }

    @ExceptionHandler(value = {DuplicateException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralResponse handleDuplicateException(Exception e) {
        return generalErrorResponse(e, false);
    }

    @ExceptionHandler(value = {UnknownErrorException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public GeneralResponse handleUnknownErrorException(Exception e) {
        return generalErrorResponse(e, false);
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public GeneralResponse handleAccessDeniedException(Exception e) {
        return generalErrorResponse(e, false);
    }

    @ExceptionHandler(value = {InvalidInputException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public GeneralResponse handleInvalidInputException(Exception e) {
        return generalErrorResponse(e, false);
    }

    @ExceptionHandler(value = {Exception.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public GeneralResponse unhandledException(Exception e) {
        return generalErrorResponse(e, true);
    }

    private GeneralResponse generalErrorResponse(Exception e, boolean hideMessage) {
        GeneralResponse response = new GeneralResponse();

        String errorId = UUID.randomUUID().toString();
        response.setErrorId(errorId);
        if (hideMessage) {
            response.setErrorMessage("Unknown Error");
        } else {
            response.setErrorMessage(MyStringUtils.isEmpty(e.getMessage()) ? "Unknown Error" : e.getMessage());
        }
        response.setStatus(MyResponseStatus.failed.toString());

        log.error("Error ID: " + errorId + ", error Message: " + e.getMessage(), e);
        return response;
    }

}
