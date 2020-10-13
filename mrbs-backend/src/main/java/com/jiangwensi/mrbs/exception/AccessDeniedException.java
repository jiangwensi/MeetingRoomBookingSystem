package com.jiangwensi.mrbs.exception;

/**
 * Created by Jiang Wensi on 21/8/2020
 */
public class AccessDeniedException extends RuntimeException{
    public AccessDeniedException(String message) {
        super(message);
    }
}
