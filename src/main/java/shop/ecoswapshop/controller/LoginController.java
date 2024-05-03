package shop.ecoswapshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import shop.ecoswapshop.domain.Member;
import shop.ecoswapshop.repository.MemberRepository;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
public class LoginController {

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/members/login")
    public String login() {
        return "members/login";
    }

    @PostMapping("/members/login")
    public String login(String username, String password, HttpSession session) {
        Member member = memberRepository.findByUsername(username);

        // 사용자가 없거나 비밀번호가 일치하지 않으면
        if (member == null || !passwordEncoder.matches(password, member.getPassword())) {
            return "redirect:/members/login?error=true";
        }

        // 로그인 성공
        session.setAttribute("member", member);
        return "redirect:/"; // 또는 로그인 성공 후 원하는 페이지로 리다이렉트
    }

    @GetMapping("/members/logout")
    public String logout(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        SecurityContextHolder.clearContext(); // SecurityContext 초기화
        session.invalidate(); // 세션 무효화

        // 쿠키 삭제
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }

        return "redirect:/"; // 로그아웃 후 홈 페이지로 이동
    }
}