package shop.ecoswapshop.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import shop.ecoswapshop.service.OAuth2Service;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final OAuth2Service oAuth2Service;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests -> authorizeRequests
                        .antMatchers("/members/login").permitAll()
                        .antMatchers("/members/new").permitAll() // 회원 가입 페이지 허용
                        .antMatchers("/categories").permitAll()
                        .antMatchers("/login/oauth2/naver").permitAll() // 네이버 로그인 경로에 대한 접근 허용
                        .antMatchers("/categories/new").hasAnyRole("ADMIN", "TYPE_ADMIN")
                        .antMatchers("/products/{productId}/edit").hasAnyRole("USER", "ADMIN") // 수정 권한 설정
                        .antMatchers("/favorite/**").hasAnyRole("USER", "ADMIN") // 수정 권한 설정
                        .antMatchers("/css/**").permitAll() // CSS 리소스 허용
                        .antMatchers("/ws/**").permitAll()
                        .anyRequest().permitAll()) // 나머지 경로는 인증 없이 허용
                .formLogin(formLogin -> formLogin
                        .loginPage("/members/login")
                        .loginProcessingUrl("/members/login")
                        .successForwardUrl("/") // 로그인 성공 시 이동할 페이지
                        .failureUrl("/members/login?error=true") // 로그인 실패 시 이동할 페이지
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/members/logout")
                        .logoutSuccessUrl("/") // 로그아웃 성공 시 이동할 페이지
                        .invalidateHttpSession(true) // 세션 무효화
                        .deleteCookies("JSESSIONID") // 세션 쿠키 삭제
                        .permitAll())
                // 스프링 시큐리티 설정 클래스 내에 로그인 성공 URL 및 네이버 로그인 경로에 대한 접근 허용 코드 추가
                .oauth2Login(oauth2Login -> oauth2Login
                        .defaultSuccessUrl("/", true) // 로그인 성공시 이동할 URL
                        .userInfoEndpoint() // 사용자가 로그인에 성공하였을 경우,
                        .userService(oAuth2Service) // 해당 서비스 로직을 타도록 설정
                );

        return http.build();
    }
}
