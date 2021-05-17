package com.jacky.mvc.service;

import org.springframework.stereotype.Service;

@Service
public class TestService {

    public TestService(){
        System.err.println("root context service init ...");
    }

    public String test(){
        return "test string";
    }
}
