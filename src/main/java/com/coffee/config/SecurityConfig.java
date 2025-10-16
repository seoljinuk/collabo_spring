package com.coffee.config;

import com.coffee.handler.CustomLoginFailureHandler;
import com.coffee.handler.CustomLoginSuccessHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        String[] permitAllowed = {"/", "/member/signup", "/member/login", "/product",
                "/product/list", "/cart/**", "/order/**", "/fruit/**", "/element/**", "/images/**"} ;

        String[] neededAuthenticated = {"/product/detail/**"} ;

        http
                .csrf(csrf -> csrf.disable())
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(permitAllowed).permitAll()
                        .requestMatchers(neededAuthenticated).authenticated()
                        .anyRequest().authenticated()
                );

        // handler()는 이 문서의 하단에 정의되어 있습니다.
        http.formLogin(form -> form
                .loginProcessingUrl("/member/login") // React에서 로그인시 요청할 url
                .usernameParameter("email") // 로그인시 id 역할을 할 컬럼명
                .passwordParameter("password") // 비밀 번호 컬럼명
                .permitAll() // 누구든지 접근 허용
                .successHandler(handler()) // 로그인 성공시 수행할 동작을 여기에 명시
                .failureHandler(failureHandler()) // 로그인 실패시
        );

        // 참고 : logoutSuccessHandler()도 있음.
        http.logout(logout -> logout
                .logoutUrl("/member/logout") // 로그 아웃시 이동할 url
                .permitAll()  // 누구든지 접근 허용
        );

        return http.build() ;
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true); // 쿠키, 세션 인증 정보 허용

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source ;
    }

    @Bean // PasswordEncoder : 비밀 번호 암호화를 해주는 인터페이스입니다.
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean // 개발자가 정의한 "로그인 성공 핸들러" 객체
    public CustomLoginSuccessHandler handler(){
        return new CustomLoginSuccessHandler();
    }


    @Bean // 개발자가 정의한 "로그인 성공 핸들러" 객체
    public CustomLoginFailureHandler failureHandler(){
        return new CustomLoginFailureHandler();
    }

}
