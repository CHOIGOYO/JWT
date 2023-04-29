package com.choigoyo.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@NoArgsConstructor // 기본 생성자
@Data
@Entity
@Table(name ="UserEntityJWT")// db에 생성될 테이블 이름
public class UserEntityJWT {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // id값 자동 증감
    private Long id; // 회원번호
    private String userName; // 회원 이름
    private String password; // 회원 비밀번호
    private String role; // 회원 역할 User, Manager ,Admin

    public List<String> getRoleList(){
        if (this.role.length() > 0) {
            return Arrays.asList(this.role.split(",")); // 콤마 기준으로 잘라서 반환
        }
        return new ArrayList<>(); // null이 뜨는 것을 방지하기 위해 임시로 리스트 반환
    }

}
