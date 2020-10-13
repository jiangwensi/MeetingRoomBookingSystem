package com.jiangwensi.mrbs.model.response.auth;

import com.jiangwensi.mrbs.model.response.GeneralResponse;

import java.util.Date;

/**
 * Created by Jiang Wensi on 17/8/2020
 */
public class VerifyEmailResponse extends GeneralResponse {

    private boolean isValid;
    private Date date;

    public VerifyEmailResponse() {
        this.date = new Date();
    }


    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
