package shop.ecoswapshop.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

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
                .authorizeRequests(authorizeRequests -> authorizeRequests
                        .antMatchers("/members/login").authenticated() // members 관련 경로는 인증 필요
                        .anyRequest().permitAll()) // 나머지 경로는 인증 없이 허용
                .formLogin(formLogin -> formLogin
                        .loginPage("/members/login")
                        .permitAll())
                .logout(logout -> logout
                        .logoutUrl("/members/logout")
                        .permitAll());
        return http.build();
    }
}
