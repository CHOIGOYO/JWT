package com.choigoyo.filter;


import javax.servlet.*;
import java.io.IOException;

public class Filter1 implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        System.out.println("필터1");
        chain.doFilter(request,response);

    }
}
