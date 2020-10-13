package com.jiangwensi.mrbs.model.response;

/**
 * Created by Jiang Wensi on 22/8/2020
 */
public class GeneralResponse {
    private String status = "";
    private String message = "";
    private String errorMessage = "";
    private String errorId = "";

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorId() {
        return errorId;
    }

    public void setErrorId(String errorId) {
        this.errorId = errorId;
    }
}
