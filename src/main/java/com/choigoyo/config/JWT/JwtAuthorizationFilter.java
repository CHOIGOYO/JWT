package com.choigoyo.config.JWT;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.choigoyo.config.auth.PrincipalDetails;
import com.choigoyo.entity.UserEntityJWT;
import com.choigoyo.repository.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    // BasicAuthenticationFilter Spring Security에서 제공하는 필터 중 하나로, HTTP 요청이 들어올 때 기본 인증(Basic Authentication)을 처리하는 역할
    // 인증이나 권한이 필요한 주소요청이 있을 때 해당 필터를 거치게됨
    private UserRepository userRepository;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, UserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("인증이나 권한이 필요한 주소가 요청되었습니다.");
        String jwtHeader = request.getHeader("Authorization"); // header 값을 확인해서 출력해보기
        System.out.println("jwtHeader : "+jwtHeader);
        // JWT 토큰을 검증해서 정상적인 사용자인지 확인
        if (jwtHeader == null || !jwtHeader.startsWith("Bearer ")) { // header에 토큰이 존재하지 않거나 토큰의 시작이 Bearer이 아니라면
            chain.doFilter(request,response); // 다시 필터를 거치게
            return;
        }

        // Authorization 에서 토큰값만 저장하기위해  "Bearer "을 빈 문자열로 변경하여 저장 (토큰값만 뽑아내는 작업)
        String jwtToken = request.getHeader("Authorization").replace("Bearer ", "");
        // 서명하여 user의 이름을 가져온다.
        String userName = JWT.require(Algorithm.HMAC512("server-secret")).build().verify(jwtToken)
                .getClaim("username").asString();
        System.out.println("================ username ==================");
        System.out.println("userName : "+userName);
        // 서명이 정상적으로 되면 if문 실행
        if (userName != null) {
            UserEntityJWT userEntityJWT = userRepository.findByUserName(userName);
            System.out.println("=================== 토큰으로 사용자 정보 찾기 ===================");
            System.out.println("userEntityJWT : "+userEntityJWT);
            PrincipalDetails principalDetails = new PrincipalDetails(userEntityJWT);

            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(
                            principalDetails, //나중에 컨트롤러에서 DI해서 쓸 때 사용하기 편함.
                            null, // 패스워드는 모르니까 null 처리
                            principalDetails.getAuthorities());

            // 강제로 세션공간에 접근하여 Authentication 객체를 세션 영역에 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
            chain.doFilter(request,response); // 다시 필터를 거치게
        }

    }
}
