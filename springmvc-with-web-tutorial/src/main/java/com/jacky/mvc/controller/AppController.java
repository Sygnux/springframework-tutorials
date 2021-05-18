package com.jacky.mvc.controller;

import com.jacky.mvc.service.root.RootService;
import com.jacky.mvc.service.app.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AppController {

    @Autowired
    private RootService rootService;
    @Autowired
    private AppService appService;

    public AppController() {
        System.out.println("AppController init ...");
    }

    @GetMapping("rootGreeting")
    public String greeting() {
        return rootService.greeting();
    }

    @GetMapping("appGreeting")
    public String appGreeting() {
        return appService.appGreeting();
    }

}
