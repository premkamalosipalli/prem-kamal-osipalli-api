package com.osipalli.template.services.system.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user/")
public class HomeController {
    @GetMapping("hello")
    public String hello() {
        return "hello";
    }

    @GetMapping("hello1")
    public String hello1() {
        return "hello";
    }
}
