package com.coffee.service;

import com.coffee.entity.Member;
import com.coffee.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/*
UserDetailsService 인터페이스
    로그인시 입력한 email 정보를 기반으로 db에서 사용자 정보를 조회하고.
    인증에 필요한 UserDetails 객체를 반환합니다.

UserDetails 인터페이스
    로그인시 사용할 id, password, 계정 만료 여부, 계정 잠금 여부, 비밀 번호 만료 여부 등등
*/

// 해당 클래스는 로그인시 입력한 사용자 정보를 토대로 데이터 베이스에서 읽은 다음 "인증" 객체로 반환하는 역할을 합니다.
// 개발자가 직접 호출할 필요는 없고, Spring Security가 암시적으로 호출합니다.
@Service
@RequiredArgsConstructor // final 키워드와 연관
public class CustomUserDetailsService implements UserDetailsService {
    private final MemberRepository memberRepository ;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // 주의) loadUserByUsername 메소드에 들어오는 매개 변수는 로그인시 사용했던 식별자(email)입니다.
        Member member = memberRepository.findByEmail(email) ;
        System.out.println("[" + member + "]");
        if(member == null){
            String message = "이메일이 " + email + "인 회원은 존재하지 않습니다." ;
            throw new UsernameNotFoundException(message);
        }

        // Spring Security는 UserDetails의 구현체인 User를 사용하여 사용자 인증(Authentication)과 권한(Authorization)을 수행합니다.
        return User.builder()
                .username(member.getEmail()) // 로그인시 사용했던 ID(사실은 email)
                .password(member.getPassword()) // 데이터 베이스에 저장된 암호화된 비밀 번호
                .roles(member.getRole().name()) // 사용자의 권한 정보(Role.USER, Role.ADMIN 등등 )
                .build();
    }
}
