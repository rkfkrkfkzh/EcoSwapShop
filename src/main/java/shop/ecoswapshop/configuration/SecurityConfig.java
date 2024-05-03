package shop.ecoswapshop.configuration;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import shop.ecoswapshop.service.CustomOAuth2UserService;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
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
                .oauth2Login()
                .userInfoEndpoint()
                .userService(customOAuth2UserService);

        return http.build();
    }

    /* static 관련 인증 설정 무시 */
    public void configure(WebSecurity web) throws Exception{
        web
                .ignoring().antMatchers("/css/**", "/js/**", "/img/**", "/error");
    }
}
