package com.choigoyo.config.JWT;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.choigoyo.config.auth.PrincipalDetails;
import com.choigoyo.entity.UserEntityJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

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
        // 3 PrincipalDetails 를 세션에 담는다 (권한 관리를 위해)
        // 4 JWT 토큰을 만들어서 응답해준다

        try {
            // request.getInputStream() 에 담겨있는 값 확인하기
//            BufferedReader reader = request.getReader();
//            String input = null;
//            while ((input = reader.readLine())!= null) {
//                System.out.println(input); // 보내온 값을 한줄씩 출력


            // json 데이터를 파싱해줌
            ObjectMapper objectMapper = new ObjectMapper();
            UserEntityJWT userEntityJWT = objectMapper.readValue(request.getInputStream(), UserEntityJWT.class);
            // 유저에 정보가 잘 담겼는지 print 해보기
            System.out.println(userEntityJWT);
            // 토큰 생성(userName, password 를 담은)
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                    new UsernamePasswordAuthenticationToken(userEntityJWT.getUserName(),userEntityJWT.getPassword());
            // authenticationManager.authenticate() 인증 수행하기
            // 그리고 PrincipalDetailsService 의 loadByUsername() 함수가 실행됨
            Authentication authenticate = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
            System.out.println(request.getInputStream()); // request.getInputStream() 은 HttpServletRequest 객체로부터 입력 스트림을 가져오는 데 사용
            // 클라이언트 또는 다른 웹브라우저에서 서버로 전송된 데이터를 읽는데 사용할 수 있는 InputStream 객체
            // authenticate 객체가 session 영역에 저장됨
            PrincipalDetails principalDetails = (PrincipalDetails)authenticate.getPrincipal();
            System.out.println("==================로그인 완료=================="); // 구분선
            System.out.println("principalDetails userName :"+principalDetails.getUser().getUserName());
            System.out.println("==============================================="); // 구분선
            return authenticate;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    // 순서 ->  attemptAuthentication 에서 인증이 정상적으로 실행되고 successfulAuthentication 메서드 실행
    // JWT 토큰을 만들어서 request 요청한 사용자에게 토큰을 응답과함께 전달
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("사용자 인증이 완료되어 successfulAuthentication 메서드가 실행됩니다.");

        PrincipalDetails principalDetails = (PrincipalDetails)authResult.getPrincipal();

        // 빌드패턴으로 토큰 생성하기
        String jwtToken = JWT.create()
                .withSubject("JWT-TOKEN")
                .withExpiresAt(new Date(System.currentTimeMillis() + (60000 * 10))) // 토큰의 유효시간을 10분으로 지정
                .withClaim("id", principalDetails.getUser().getId())
                .withClaim("username", principalDetails.getUser().getUserName())
                .sign(Algorithm.HMAC512("server-secret")); // server만 알고있는 secret값 으로 서명

        response.addHeader("Authentication","Bearer "+jwtToken);  // 사용자에게 응답

        System.out.println("==================토큰 생성==================");
        System.out.println("name : Authentication");
        System.out.println("value: Bearer "+ jwtToken);

    }
}
