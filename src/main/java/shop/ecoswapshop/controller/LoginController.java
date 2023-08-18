package shop.ecoswapshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {
    @GetMapping("/members/login")
    public String login() {
        return "members/login"; // "login"은 로그인 페이지의 뷰 이름이어야 합니다.
    }
}