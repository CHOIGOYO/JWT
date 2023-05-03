package com.choigoyo.controller;

import com.choigoyo.entity.UserEntityJWT;
import com.choigoyo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestApiController {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping("/")
    public String home(){
        return "<h1>HOME</h1>";
    }

    @PostMapping("/token") // 토큰 테스트를 위해 임시 컨트롤러 생성
    public String token(){
        return "<h1>TOKEN</h1>";
    }

    @PostMapping("join")
    public String join(@RequestBody UserEntityJWT userEntityJWT) {
        userEntityJWT.setPassword(bCryptPasswordEncoder.encode(userEntityJWT.getPassword())); // 비밀번호 암호화
        userEntityJWT.setRole("user"); // 회원가입 기본 역할 세팅
        userRepository.save(userEntityJWT);
        return "회원가입이 완료되었습니다.";
    }

    // user,manager,admin 접근 가능
    @GetMapping("/api/v1/user")
    public String user(){
        return "user";
    }

    // manager,admin 접근 가능
    @GetMapping("/api/v1/manager")
    public String manager(){
        return "manager";
    }

    // admin 접근 가능
    @GetMapping("/api/v1/admin")
    public String admin(){
        return "admin";
    }
}
