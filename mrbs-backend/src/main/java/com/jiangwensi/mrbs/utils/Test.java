package com.jiangwensi.mrbs.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Jiang Wensi on 2/9/2020
 */
public class Test {
    public static void main(String[] args){
        String dateFormat="yyyy-MM-dd";
        String toFormat="HH:mm:ss";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat+" "+toFormat);

        System.out.println(sdf.format(new Date()));

    }
}
