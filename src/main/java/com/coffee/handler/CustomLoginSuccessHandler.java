package com.coffee.handler;

import com.coffee.entity.Member;
import com.coffee.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/*
AuthenticationSuccessHandler
    스프링 시큐리티에서 로그인에 성공을 했을 때 실행하고자 하는 동작을
    개발자가 직접 정의할 수 있도록 해주는 인터페이스
    우리는 로그인 성공시 클라이언트에 JSON 형식으로 회원 정보를 반환하도록 하겠습니다.
*/
@Component
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {
    private MemberService memberService ;

    @Autowired // setter 메소드를 이용한 의존성 객체 주입(DI)
    public void setMemberService(MemberService memberService) {
        this.memberService = memberService;
    }

    @Override // 이 메소드는 로그인 성공시 자동 실행이 됩니다.(콜백 메소드)
    public void onAuthenticationSuccess(
            HttpServletRequest request,
            HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        // authentication는 인증 객체라고 하며, 로그인 성공시의 정보가 포함되어 있습니다.

        // 클라이언트에 대한 응답을 json 타입으로 지정(UTF-8 타입 인코딩 포함)
        response.setContentType("application/json;charset=UTF-8");

        User user = (User)authentication.getPrincipal();
        String email = user.getUsername() ; // 우리가 사용한 username은 사실 email입니다.
        Member member = memberService.findByEmail(email) ;

        Map<String, Object> data = new HashMap<>() ;
        data.put("message", "success"); // 로그인 성공 메시지
        data.put("member", member); // Member 객체를 JSON 형식으로 변환

        System.out.println("회원 객체 정보");
        System.out.println(member);

        // ObjectMapper는 Jackson 라이브러리에 들어 있는 자바 객체를 json 형식으로 변환해주는 클래스입니다.
        ObjectMapper mapper = new ObjectMapper() ;

        // Java 날짜, 시간 처리 모듈을 등록합니다.
        mapper.registerModules(new JavaTimeModule()) ;

        // 시간의 TimeStamp 타입 대신 문자열로 변환합니다.
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS) ;

        // JSON 문자열을 http의 응답 객체로 전송합니다.
        response.getWriter().write(mapper.writeValueAsString(data)) ;
    }
}
