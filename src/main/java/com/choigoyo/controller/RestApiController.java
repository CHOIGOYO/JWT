package com.choigoyo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestApiController {


    @GetMapping("/")
    public String home(){
        return "<h1>HOME</h1>";
    }

    @PostMapping("/token") // 토큰 테스트를 위해 임시 컨트롤러 생성
    public String token(){
        return "<h1>TOKEN</h1>";
    }
}
