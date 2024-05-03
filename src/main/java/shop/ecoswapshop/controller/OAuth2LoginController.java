package shop.ecoswapshop.controller;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import shop.ecoswapshop.service.MemberService;

import java.util.Map;

@Controller
@RequiredArgsConstructor
public class OAuth2LoginController {

    private static final Logger log = LoggerFactory.getLogger(OAuth2LoginController.class);

    private final MemberService memberService;

    @GetMapping("/login/oauth2/naver")
    public String naverLoginSuccess(@AuthenticationPrincipal OAuth2AuthenticationToken authenticationToken, Model model) {
        // 인증 객체 null 체크
        if (authenticationToken == null) {
            log.info("OAuth2AuthenticationToken is null. Redirecting to login page.");
            return "redirect:/members/login"; // 로그인 페이지나 오류 페이지로 리다이렉트할 수 있습니다.
        }

        Map<String, Object> attributes = authenticationToken.getPrincipal().getAttributes();

        String email = (String) attributes.get("email"); // 네이버 사용자 정보에서 이메일 추출

        log.info("Logged in user email: {}", email);

        return "redirect:/";
    }

}
