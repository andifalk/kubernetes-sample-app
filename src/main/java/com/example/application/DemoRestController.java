package com.example.application;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DemoRestController {

    @Value("${app.myconfig}")
    private String myConfig;

    @Value("${app.mysecret}")
    private String mySecret;

    @GetMapping("/hello")
    public String hello() {
        return "Hello, World!";
    }

    @GetMapping("/config")
    public String config() {return String.format("app.myconfig=[%s], app.mysecret=[%s]", myConfig, mySecret);
    }
}
