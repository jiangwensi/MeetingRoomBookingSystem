package com.jiangwensi.mrbs.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by Jiang Wensi on 27/10/2020
 */
@Configuration
@PropertySource(value = "file:D:\\MyProjects\\mrbs\\config\\backend\\local.application.properties",
        ignoreResourceNotFound = false)
public class AppConfiguration {
}
