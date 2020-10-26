package com.jiangwensi.mrbs;

/**
 * Created by Jiang Wensi on 18/10/2020
 */
public class Test {

    public static void main(String[] args) {
        String[] a = {"a"};
        testString(a);
        System.out.println(a[0]);
    }


    private static void testString(String[] a){
        a[0]= "b";
    }


}
