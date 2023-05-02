package com.choigoyo.config.auth;

import com.choigoyo.entity.UserEntityJWT;
import com.choigoyo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// http://localhost:8081/login Spring Security 기본 로그인 요청 주소를 securityConfig 설정에 form 로그인을 사용하지 않겠다 정했기 때문에 404에러가 발생.
@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        System.out.println("PrincipalDetails 의 loadUserByUsername()");
        UserEntityJWT userEntity = userRepository.findByUserName(userName);
        return new PrincipalDetails(userEntity);
    }
}
