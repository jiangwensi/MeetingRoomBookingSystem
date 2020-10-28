package com.jiangwensi.mrbs;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by Jiang Wensi on 16/8/2020
 */
@Component
@Slf4j
public class AppProperties {

    @Autowired
    private Environment env;

    @Value("${logging.file.path}")
    private String filePath;

    @PostConstruct
    public void test() {
        System.out.println("filePath="+filePath);
    }

    public String getProperty(String key){
        return env.getProperty(key);
    }

    public static AppProperties getAppProperties() {
        return (AppProperties) AppContext.getContext().getBean("appProperties");
    }
}
