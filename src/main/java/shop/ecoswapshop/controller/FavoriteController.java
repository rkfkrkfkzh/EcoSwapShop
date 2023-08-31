package shop.ecoswapshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import shop.ecoswapshop.domain.Favorite;
import shop.ecoswapshop.service.FavoriteService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @PostMapping("favorites")
    public void addFavorite(@RequestParam Long memberId, @RequestParam Long productId) {
        favoriteService.addFavorite(memberId, productId);
    }

    @GetMapping("/favorites")
    public List<Favorite> getFavorites(@RequestParam Long memberId) {
        return favoriteService.getFavoritesByMember(memberId);
    }
}
