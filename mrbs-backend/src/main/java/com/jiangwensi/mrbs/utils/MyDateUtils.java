package com.jiangwensi.mrbs.utils;

import com.jiangwensi.mrbs.AppProperties;
import lombok.extern.slf4j.Slf4j;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jiang Wensi on 2/9/2020
 */
@Slf4j
public class MyDateUtils {

    public static boolean isValidFormat(String value){
        String dateformat = AppProperties.getAppProperties().getProperty("dateformat");
        String timeformat = AppProperties.getAppProperties().getProperty("timeformat");

        SimpleDateFormat sdf = new SimpleDateFormat(getDefaultDateFormat());
        try {
            Date date = sdf.parse(value);
            String result = sdf.format(date);
            return value.equals(result);
        } catch (ParseException e) {
           log.error(e.getMessage(),e);
        }
        return false;

    }

    public static String getDefaultDateFormat(){
        String dateformat = AppProperties.getAppProperties().getProperty("dateformat");
        String timeformat = AppProperties.getAppProperties().getProperty("timeformat");
        return dateformat+" "+timeformat;
    }
}
