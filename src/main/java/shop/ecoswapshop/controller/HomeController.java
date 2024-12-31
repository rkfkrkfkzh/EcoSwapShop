package shop.ecoswapshop.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shop.ecoswapshop.domain.Member;
import shop.ecoswapshop.domain.Product;
import shop.ecoswapshop.service.MemberService;
import shop.ecoswapshop.service.ProductService;

import java.util.Optional;

@Slf4j
@Controller
@RequiredArgsConstructor
public class HomeController {

    private final ProductService productService;
    private final MemberService memberService;

    // 로그인한 사용자의 ID를 가져오는 메서드
    private Optional<Long> getLoggedInMemberId() {
        return memberService.findLoggedInMemberId();
    }

    // 로그인한 사용자의 전체 정보를 가져오는 메서드
    private Optional<Member> getLoggedMember() {
        return getLoggedInMemberId().flatMap(memberService::findMemberById);
    }

    // 로그인한 사용자의 정보를 가져와 모델에 추가, 로그인하지 않았을 경우 로그를 남김
    @GetMapping("/")
    public String home(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Product> pagedProducts = productService.getPagedProducts(page, 6);
        model.addAttribute("pagedProducts", pagedProducts);

        // 값이 존재할 경우 첫 번째 람다를 실행, 존재하지 않을 경우 두 번째 람다를 실행
        getLoggedMember()
                .ifPresentOrElse(
                        member -> {
                            model.addAttribute("loggedMember", member);
                            model.addAttribute("loggedInMemberId", member.getId());
                        },
                        () -> log.info("User not logged in"));
        return "home";
    }
}
