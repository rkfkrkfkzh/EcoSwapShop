package shop.ecoswapshop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.JpaRepositoryImplementation;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import shop.ecoswapshop.domain.Product;
import shop.ecoswapshop.service.ProductService;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProductService productService;

    @GetMapping("/")
    public String home(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        List<Product> products = productService.findAllProducts();

        model.addAttribute("products", products);

        if (auth != null) {
            Object principal = auth.getPrincipal();
            if (principal instanceof UserDetails) {
                String username = ((UserDetails) principal).getUsername();
                System.out.println("로그인 사용자 이름: " + username);
            } else {
                System.out.println("로그인 사용자 정보를 얻을 수 없음");
            }
        } else {
            System.out.println("로그인 되지 않은 사용자");
        }

        return "home";

    }
}
