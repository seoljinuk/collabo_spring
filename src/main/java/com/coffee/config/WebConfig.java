package com.coffee.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // 해당 클래스를 객체로 만들어 주되, 이 파일은 설정용 파일입니다.
public class WebConfig implements WebMvcConfigurer {
    // 자바에서 ...은 가변 매개 변수
    @Override // allowCredentials(true) : 브라우저가 서버와 통신할 때 쿠키/세션 등의 인증 정보를 주고 받는 것에 대하여 허락하겠습니다.
    public void addCorsMappings(CorsRegistry registry) {
        // 3000번 포트에서 GET부터 PATCH까지의 열거한 요청들을 모두 수락하겠습니다.
        registry.addMapping("/**") /* 모든 경로 허용  */
                .allowedOrigins("http://localhost:3000") /* react 포트 */
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")  /* 허용할 메소드 */
                .allowCredentials(true) ; // 쿠키 전송 허용
    }

    // application.properties 파일에서 uploadPath 항목의 값을 변수에 할당해 줍니다.
    @Value("${uploadPath}")
    private String uploadPath ; // file:///C:/shop/images/

    @Override // 외부에서 "/images/**"란 요청이 들어 오면, uploadPath를 한번 찾아 보세요.
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/images/**")
                .addResourceLocations(uploadPath);
    }
}
