package com.jiangwensi.mrbs.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jiang Wensi on 23/8/2020
 */
public class MyStringUtils {

    public static String toLowerCaseAndTrim(String str){
        return str==null?null:str.toLowerCase().trim();
    }

    public static List<String> toLowerCaseAndTrim(List<String> strList){
        if(strList==null){
            return null;
        }

        List<String> returnValue = new ArrayList<>();

        for(String s: strList) {
            returnValue.add(MyStringUtils.toLowerCaseAndTrim(s));
        }
        return returnValue;
    }

    public static String toUpperCaseAndTrim(String str){
        return str==null?null:str.toUpperCase().trim();
    }

    public static List<String> toUpperCaseAndTrim(List<String> strList){
        if(strList==null){
            return null;
        }

        List<String> returnValue = new ArrayList<>();

        for(String s: strList) {
            returnValue.add(MyStringUtils.toUpperCaseAndTrim(s));
        }
        return returnValue;
    }

    public static boolean isEmpty(String str){
        return str == null || str.trim().equals("");
    }
}
