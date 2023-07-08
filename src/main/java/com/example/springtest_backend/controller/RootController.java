package com.example.springtest_backend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    @GetMapping("/")
    public String root(){
        return "This request will be recorded";
    }

    @GetMapping("/test")
    public String test(){
        return "This request will be recorded";
    }

}
