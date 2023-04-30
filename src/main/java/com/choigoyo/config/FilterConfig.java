package com.choigoyo.config;

import com.choigoyo.filter.Filter2;
import com.choigoyo.filter.Filter3;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration // IoC (Inversion of Control) 제어의 역전을 의미
public class FilterConfig {

    @Bean
    public FilterRegistrationBean<Filter3> filter3(){
        FilterRegistrationBean<Filter3> bean = new FilterRegistrationBean<>(new Filter3());
        bean.addUrlPatterns("/*"); // 모든 요청에서 필터를 적용
        bean.setOrder(0); // 필터의 순서 지정 / 우선순위 가장 높음
        return bean;
    }
    @Bean
    public FilterRegistrationBean<Filter2> filter2(){
        FilterRegistrationBean<Filter2> bean = new FilterRegistrationBean<>(new Filter2());
        bean.addUrlPatterns("/*"); // 모든 요청에서 필터를 적용
        bean.setOrder(1); // 필터의 순서 지정
        return bean;
    }
}
