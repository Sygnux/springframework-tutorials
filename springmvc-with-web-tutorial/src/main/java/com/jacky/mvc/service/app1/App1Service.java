package com.jacky.mvc.service.app1;

import org.springframework.stereotype.Service;

@Service
public class App1Service {

    public App1Service(){
        System.out.println("子容器 App1 service初始化 ...");
    }

    public String app1Greeting(){
        return "app1 service greeting";
    }
}
