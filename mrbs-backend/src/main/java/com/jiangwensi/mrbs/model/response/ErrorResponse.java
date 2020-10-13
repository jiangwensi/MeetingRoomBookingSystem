package com.jiangwensi.mrbs.model.response;

import java.util.Date;

/**
 * Created by Jiang Wensi on 16/8/2020
 */
public class ErrorResponse {
    private Date time;
    private String errorMessage;

    public ErrorResponse() {
    }

    public ErrorResponse(String errorMessage) {
        this.time = new Date();
        this.errorMessage = errorMessage;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
