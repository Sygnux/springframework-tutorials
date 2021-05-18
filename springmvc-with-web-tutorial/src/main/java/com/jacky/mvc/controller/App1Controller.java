package com.jacky.mvc.controller;

import com.jacky.mvc.service.root.RootService;
import com.jacky.mvc.service.app1.App1Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class App1Controller {

    @Autowired
    private RootService rootService;
    @Autowired
    private App1Service app1Service;

    public App1Controller(){
        System.out.println("App1Controller init ...");
    }

    @GetMapping("rootGreeting")
    public String greeting(){
        return rootService.greeting();
    }

    @GetMapping("app1Greeting")
    public String app1Greeting(){
        return app1Service.app1Greeting();
    }

}
