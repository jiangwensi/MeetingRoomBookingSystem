package com.jiangwensi.mrbs;

import java.util.Base64;

/**
 * Created by Jiang Wensi on 18/10/2020
 */
public class Test {
    public static void main(String[] args) {
        String originalInput = "test input";
        System.out.println(originalInput.getBytes());
        String encodedString = Base64.getEncoder().encodeToString(originalInput.getBytes());
        System.out.println(encodedString);
        System.out.println(new String(Base64.getDecoder().decode(encodedString)));
    }
}
