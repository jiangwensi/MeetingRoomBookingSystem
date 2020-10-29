package com.jiangwensi.mrbs;

import java.net.URL;
import java.net.URLClassLoader;

/**
 * Created by Jiang Wensi on 18/10/2020
 */
public class Test {

    public static void main (String[] args) {

        ClassLoader cl = ClassLoader.getSystemClassLoader();

        URL[] urls = ((URLClassLoader)cl).getURLs();

        for(URL url: urls){
            System.out.println(url.getFile());
        }

    }


    private static void testString(String[] a){
        a[0]= "b";
    }


}
