package com.jacky.mvc.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {


    public RootController(){
        System.out.println("root controller init ...");
    }

    @GetMapping("rootGreeting")
    public String greeting() {
        return "root greeting";
    }
}
