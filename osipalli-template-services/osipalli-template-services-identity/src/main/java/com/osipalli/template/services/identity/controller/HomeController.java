package com.osipalli.template.services.identity.controller;


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
}
