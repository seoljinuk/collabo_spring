package com.coffee.controller;

import com.coffee.entity.Member;
import com.coffee.service.MemberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController // 해당 클래스는 회원과 관련된 웹 요청(from react)을 접수하여 처리해주는 컨트롤러 클래스입니다.
@RequiredArgsConstructor // final 키워드 또는 @NotNull 필드가 들어 있는 식별자에 생성자를 통하여 값을 외부에서 주입해줍니다.
public class MemberController {
    private final MemberService memberService ;

    @PostMapping("/member/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody Member bean, BindingResult bindingResult) {
        // 1) 유효성 검사 결과 확인
        if (bindingResult.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errors.put(error.getField(), error.getDefaultMessage());
            }
            return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
        }

        // 2) 이메일 중복 체크
        Member member = memberService.findByEmail(bean.getEmail());
        if (member != null) {
            return new ResponseEntity<>(Map.of("email", "이미 존재하는 이메일 주소입니다."), HttpStatus.BAD_REQUEST);
        }

        // 3) 회원가입 처리
        memberService.insert(bean);
        return new ResponseEntity<>("회원 가입 성공", HttpStatus.OK);
    }
}
