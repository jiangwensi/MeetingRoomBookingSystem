package com.jiangwensi.mrbs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Created by Jiang Wensi on 16/8/2020
 */
@Component
public class AppProperties {

    @Autowired
    private Environment env;

    public String getProperty(String key){
        return env.getProperty(key);
    }

    public static AppProperties getAppProperties() {
        return (AppProperties) AppContext.getContext().getBean("appProperties");
    }
}
