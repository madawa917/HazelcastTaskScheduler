package com.madawa.scheduler.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MainController {

    @GetMapping("/home")
    public String hello() throws InterruptedException {

        System.out.println("Welcome");
        return "Welcome to Task Scheduler App by Madawa";
    }

}
