package com.jacky.mvc.service.root;

import org.springframework.stereotype.Service;

@Service
public class RootService {

    public RootService(){
        System.out.println("父容器service初始化 ...");
    }

    public String greeting(){
        return "root greeting";
    }
}
