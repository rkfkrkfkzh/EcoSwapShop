package shop.ecoswapshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shop.ecoswapshop.domain.Favorite;
import shop.ecoswapshop.domain.Member;
import shop.ecoswapshop.service.FavoriteService;
import shop.ecoswapshop.service.MemberService;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;
    private final MemberService memberService;

    // 로그인한 사용자의 memberId를 가져옵니다.
    private Optional<Long> getLoggedInMemberId() {
        return memberService.findLoggedInMemberId();
    }

    private Member getLoggedInMember() {
        return getLoggedInMemberId()
                .map(memberService::findMemberById)
                .orElseThrow(() -> new RuntimeException("Logged in member not found"))
                .orElseThrow(()-> new RuntimeException("Member Not Found"));
    }

    private boolean isUserAuthorized(Long memberId) {
        return getLoggedInMemberId().isPresent() && getLoggedInMemberId().get().equals(memberId);

    }

    @GetMapping
    public String getFavorites(Model model) {
        if (getLoggedInMemberId().isPresent()) {
            List<Favorite> favorites = favoriteService.getFavoritesByMember(getLoggedInMemberId().get());
            model.addAttribute("favorites", favorites);
            getLoggedInMemberId().ifPresent(memberId -> model.addAttribute("loggedInMemberId", memberId));

        } else {
            return "redirect:/error";
        }
        return "favorites/favoriteList";
    }

    @PostMapping("/new")
    public String addFavorite(@RequestParam Long memberId, @RequestParam Long productId) {
        favoriteService.addFavorite(memberId, productId);
        return "redirect:/favorites/list";
    }

}
