package com.jacky.mvc.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("test")
public class TestController {

    public TestController(){
        System.err.println("TestController init ...");
    }

    @GetMapping("greeting")
    public String greeting(){
        return "Hello";
    }
}
