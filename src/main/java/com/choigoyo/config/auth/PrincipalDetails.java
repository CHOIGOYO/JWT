package com.choigoyo.config.auth;

import com.choigoyo.entity.UserEntityJWT;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

@Data
@NoArgsConstructor // 기본 생성자
public class PrincipalDetails implements UserDetails {
    private UserEntityJWT user;

    // 생성자
    public PrincipalDetails (UserEntityJWT user){
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        /*
        *  user.getRoleList()를 통해 해당 유저가 가지고 있는 모든 역할(Role)을 가져온 뒤, 각각의 역할에 대한 GrantedAuthority 객체를 생성하여 이를 authorities 리스트에 추가
        *  GrantedAuthority는 권한 정보를 나타내는 인터페이스
        * */
        user.getRoleList().forEach(r ->{
            authorities.add(() -> r);
        });
        return null;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUserName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
