package com.choigoyo.config;

import com.choigoyo.config.JWT.JwtAuthenticationFilter;
import com.choigoyo.filter.Filter1;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;


@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final CorsFilter corsFilter;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity.addFilterBefore(new Filter1(),BasicAuthenticationFilter.class); // Filter의 순서를 지정해줌 // 이 방법 말고 FilterConfig에서 설정하도록 변경
        httpSecurity.addFilterBefore(new Filter1(), BasicAuthenticationFilter.class); // 필터실행 우선순위 체크 테스트를 위해 추가
        httpSecurity.csrf().disable();
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // session을 사용하지 않는다
                .and()
                .addFilter(corsFilter)  // @CrossOrigin(인증 없을 때) , 시큐리티 필터에 등록(인증 있을 때)
                .formLogin().disable() // id pw 로그인을 form 로그인을 하지 X
                .httpBasic().disable()
                .addFilter(new JwtAuthenticationFilter(authenticationManager())) // JWT 패키지에 있는 filter 등록
                .authorizeRequests()
                .antMatchers("/api/v1/user/**")
                .access("hasRole('user') or hasRole('manager') or hasRole('admin')")
                .antMatchers("/api/v1/manager/**")
                .access("hasRole('manager') or hasRole('admin')")
                .antMatchers("/api/v1/admin/**")
                .access("hasRole('admin')")
                .anyRequest().permitAll(); // 설정 외 모든 경로는 인증없이 접근가능
    }
}
