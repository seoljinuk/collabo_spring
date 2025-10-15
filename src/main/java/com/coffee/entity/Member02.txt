package com.coffee.entity;

import com.coffee.constant.Role;
import jakarta.persistence.* ;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

// 회원 1명에 대한 정보를 저장하고 있는 자바 클래스
@Getter @Setter @ToString
@Entity // 해당 클래스를 엔터티로 관리해 주세요.
@Table(name = "members") // 테이블 이름은 "members"으로 생성해 주세요.
public class Member {
    @Id // 이 컬럼은 primary key입니다.
    @GeneratedValue(strategy = GenerationType.AUTO) // 기본키 생성 전략
    @Column(name = "member_id") // 컬럼 이름을 변경합니다.
    private Long id;

    @NotBlank(message = "이름은 필수 입력 사항입니다.")
    private String name;

    // 필수 사항이고, 절대로 동일한 값이 들어오면 않됩니다.
    @Column(unique = true, nullable = false)
    @NotBlank(message = "이메일은 필수 입력 사항입니다.")
    @Email(message = "올바른 이메일 형식으로 입력해 주셔야 합니다.")
    private String email;

    // @Pattern는 정규 표현식을 의미합니다.
    // [asdf] : 대괄호 사이에 들어 있는 글자 1개와 매칭
    @NotBlank(message = "비밀 번호는 필수 입력 사항입니다.")
    @Size(min = 8, max = 255, message = "비밀 번호는 8자리 이상, 255자리 이하로 입력해 주세요.")
    @Pattern(regexp = ".*[A-Z].*", message = "비밀 번호는 대문자 1개 이상을 포함해야 합니다.")
    @Pattern(regexp = ".*[!@#$%].*", message = "비밀 번호는 특수 문자 '!@#$%' 중 하나 이상을 포함해야 합니다.")
    private String password;

    @NotBlank(message = "주소는 필수 입력 사항입니다.")
    private String address;

    @Enumerated(EnumType.STRING) // 컬럼에 문자열 형식으로 데이터가 들어 감.
    private Role role; // 일반인 또는 관리자
    
    private LocalDate regdate ; // 등록 일자
}
