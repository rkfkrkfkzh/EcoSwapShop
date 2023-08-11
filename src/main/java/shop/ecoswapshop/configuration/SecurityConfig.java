package shop.ecoswapshop.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.LogoutConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(requests -> requests
                        .mvcMatchers("/transplantable").hasAnyRole("SCHEDULER", "LEHRER", "UNWAVERING")
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .logout(LogoutConfigurer::permitAll);
        return http.build();
    }
}
/*
~ 안에antMatchers()( 뿐만 아니라 mvcMathcers() 그리고 regexMatchers())는 Spring Security 6.0에서 더 이상 사용되지 않고 제거되었습니다.
따라서 Spring Boot 3 프로젝트에서는 사용할 수 없습니다.
 */
/*
클래스 선언: SecurityConfig 클래스는 Spring Security 설정을 담당합니다.
@Configuration, @EnableWebSecurity 어노테이션은 이 클래스가 보안 설정을 포함하고 있음을 나타냅니다.

비밀번호 인코더 Bean: passwordEncoder 메서드는 BCrypt 알고리즘을 사용하는 비밀번호 인코더를 Bean으로 등록합니다.
이것은 사용자 비밀번호를 안전하게 저장하고 검증하는 데 사용됩니다.

보안 필터 체인 Bean: securityFilterChain 메서드는 애플리케이션의 보안 규칙을 정의합니다.
여기에는 여러 보안 관련 설정이 포함됩니다:

요청 인가: .authorizeHttpRequests 메서드는 HTTP 요청에 대한 인가 규칙을 정의합니다:
requestMatchers("/vertretungsplan")은 /vertretungsplan 경로에 대한 규칙을 설정합니다.
hasAnyRole("SCHUELER", "LEHRER", "VERWALTUNG")은 해당 경로에 대해 세 가지 역할 중 하나를 가진 사용자만 접근을 허용합니다.
anyRequest().authenticated()은 그 외의 모든 요청은 인증된 사용자만 접근할 수 있도록 합니다.
폼 로그인: .formLogin 메서드는 폼 기반 로그인을 설정합니다:
loginPage("/login")은 로그인 페이지의 URL을 설정합니다.
permitAll()은 로그인 페이지에 대한 모든 사용자의 접근을 허용합니다.
로그아웃: .logout 메서드는 로그아웃 구성을 설정합니다:
LogoutConfigurer::permitAll은 로그아웃 요청에 대해 모든 사용자의 접근을 허용합니다.
보안 필터 체인 반환: http.build() 호출은 위에서 정의한 보안 규칙을 가진 SecurityFilterChain 객체를 반환합니다.
이 체인은 요청이 애플리케이션의 다른 부분으로 전달되기 전에 적용됩니다.
 */