package com.jacky.mvc.service.app;

import org.springframework.stereotype.Service;

@Service
public class AppService {

    public AppService(){
        System.out.println("子容器 App service初始化 ...");
    }

    public String appGreeting(){
        return "app service greeting";
    }
}
