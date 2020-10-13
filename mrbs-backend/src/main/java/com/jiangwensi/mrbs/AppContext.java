package com.jiangwensi.mrbs;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by Jiang Wensi on 16/8/2020
 */
@Component
public class AppContext implements ApplicationContextAware {

    Logger logger = LoggerFactory.getLogger(AppContext.class);

    @Autowired
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        logger.debug("setApplicationContext is called");
        this.context = applicationContext;
    }

    public static ApplicationContext getContext() {
        return context;
    }
}
