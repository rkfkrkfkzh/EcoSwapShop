package shop.ecoswapshop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import shop.ecoswapshop.domain.Member;
import shop.ecoswapshop.domain.Product;
import shop.ecoswapshop.service.MemberService;
import shop.ecoswapshop.service.ProductService;

import java.util.List;
import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProductService productService;
    private final MemberService memberService;

    private Optional<Long> getLoggedInMemberId() {
        return memberService.findLoggedInMemberId();
    }

    private Member getLoggedMember() {
        Optional<Long> loggedInMemberId = getLoggedInMemberId();
        if (!loggedInMemberId.isPresent()) {
            throw new RuntimeException("Logged in member not found");
        }
        Long memberId = loggedInMemberId.get();
        Optional<Member> memberById = memberService.findMemberById(memberId);
        if (!memberById.isPresent()) {
            throw new RuntimeException("Member Not Found");
        }
        return memberById.get();
    }

    @GetMapping("/")
    public String home(Model model) {
        List<Product> products = productService.findAllProducts();
        model.addAttribute("products", products);

        Optional<Long> loggedInMemberId = getLoggedInMemberId();
        if (loggedInMemberId.isPresent()) {
            Long memberId = loggedInMemberId.get();
            Optional<Member> memberById = memberService.findMemberById(memberId);
            if (memberById.isPresent()) {
                Member loggedMember = memberById.get();
                model.addAttribute("loggedMember", loggedMember);
            } else {
                log.warn("Logged in member not found");
            }
        } else {
            log.info("User not logged in");
        }
        return "home";
    }
}
