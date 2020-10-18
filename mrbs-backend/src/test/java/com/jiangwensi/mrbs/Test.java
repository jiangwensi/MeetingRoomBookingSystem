package com.jiangwensi.mrbs;

import com.jiangwensi.mrbs.enumeration.TokenType;

/**
 * Created by Jiang Wensi on 18/10/2020
 */
public class Test {
    public static void main(String[] args) {
        System.out.println(TokenType.VERIFY_EMAIL);
        String verifyEmail = "VERIFY_EMAIL";
        System.out.println(verifyEmail);
        if(TokenType.VERIFY_EMAIL.name().equals(verifyEmail)) {
            System.out.println("same");
        } else {
            System.out.println("different");
        }
    }
}
