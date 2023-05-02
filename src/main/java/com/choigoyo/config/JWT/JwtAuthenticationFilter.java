package com.choigoyo.config.JWT;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    /**
     * Spring Security 의 UsernamePasswordAuthenticationFilter 를 확장하여 사용하게되면
     * http://localhost:8081/login 주소로 userName,password 를 post 형식으로 전송하면
     * UsernamePasswordAuthenticationFilter 가 동작함 SecurityConFig 클래스에 form 로그인을 비활성화 시켜서 동작하지 않는 상태이나,
     * JwtAuthenticationFilter 를 다시 SecurityConFig 클래스에 등록해주면 동작하게됨
     */

    private final AuthenticationManager authenticationManager;


    // http://localhost:8081/login 요청이 들어오면 authenticationManager 통해서 로그인시도
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter : 로그인 시도중...");

        // 1 사용자의 userName, password 를 받는다
        // 2 로그인이 가능한지 확인
        // 3 PrincipalDetails를 세션에 담는다 (권한 관리를 위해)
        // 4 JWT 토큰을 만들어서 응답해준다
        return super.attemptAuthentication(request, response);
    }
}
