package shop.ecoswapshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    @GetMapping
    public String getFavorites(@RequestParam(defaultValue = "0")int page, Model model) {
        if (memberService.findLoggedInMemberId().isPresent()) {
            Long memberId = memberService.findLoggedInMemberId().get();

            Page<Favorite> pagedFavorites = favoriteService.getFavoritesByMember(memberId, page, 8);
            model.addAttribute("pagedFavorites", pagedFavorites);
            model.addAttribute("loggedInMemberId", memberId);
            return "favorites/favoriteList";
        } else {
            return "redirect:/error";
        }
    }

    @PostMapping("/toggle")
    public String toggleFavorite(@RequestParam Long memberId, @RequestParam Long productId) {
        if (!memberService.isUserAuthorized(memberId)) {
            return "redirect:/error";
        }
        favoriteService.toggleFavorite(memberId, productId);
        return "redirect:/favorites";
    }
}
