package com.jiangwensi.mrbs.exception;

/**
 * Created by Jiang Wensi on 17/8/2020
 */
public class UnknownErrorException extends RuntimeException{
    public UnknownErrorException(String message) {
        super(message);
    }
}
