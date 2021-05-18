package com.jacky.mvc.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackages = "com.jacky.mvc")
public class AppConfig {

    public AppConfig(){
        System.out.println("app config init ...");
    }
}
