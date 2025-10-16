package com.coffee.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        // 3000번 포트에서 GET부터 PATCH까지의 열거한 요청들을 모두 수락하겠습니다.
//        registry.addMapping("/**") /* 모든 경로 허용  */
//                .allowedOrigins("http://localhost:3000") /* react 포트 */
//                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")  /* 허용할 메소드 */
//                .allowCredentials(true) ; // 쿠키 전송 허용
//    }

    @Value("${uploadPath}")
    private String uploadPath ; // file:///C:/shop/images/

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
                .addResourceHandler("/images/**")
                .addResourceLocations(uploadPath);
    }
}
