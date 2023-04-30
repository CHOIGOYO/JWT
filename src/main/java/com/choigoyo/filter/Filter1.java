package com.choigoyo.filter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class Filter1 implements javax.servlet.Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest)request;
        HttpServletResponse res = (HttpServletResponse)response;

        // post 요청일 경우에만
        // 토큰이 넘어오면 인증 시도 넘어오지 않으면 컨트롤러로 접근 불가
        if (req.getMethod().equals("POST")) {
            System.out.println("POST 요청이 확인되었습니다.");
            String authorization = req.getHeader("Authorization");
            System.out.println(authorization);
            System.out.println("필터1");

            // header 의 Authorization 이 choigoyo 일때만 체인필터를 타도록 조건 걸기
            if (authorization.equals("choigoyo")) {
                chain.doFilter(req, res);
            } else {
                PrintWriter out = res.getWriter();
                out.println("인증 실패");
            }
        }
    }
}
