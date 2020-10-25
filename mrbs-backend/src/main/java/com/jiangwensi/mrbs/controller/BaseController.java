package com.jiangwensi.mrbs.controller;

import com.jiangwensi.mrbs.constant.MyResponseStatus;
import com.jiangwensi.mrbs.model.response.GeneralResponse;

/**
 * Created by Jiang Wensi on 19/10/2020
 */
public class BaseController {

    protected GeneralResponse generalSuccessResponse(String message){
        GeneralResponse returnValue = new GeneralResponse();
        returnValue.setStatus(MyResponseStatus.success.name());
        returnValue.setMessage("Request is successful");
        return returnValue;
    }
}
