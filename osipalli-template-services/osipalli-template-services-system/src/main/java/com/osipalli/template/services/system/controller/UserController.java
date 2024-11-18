package com.osipalli.template.services.system.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/user")
@Slf4j
public class UserController {

    @PostMapping("/register")
    public void createUser(){
        log.debug("Creating new User");
    }
}
