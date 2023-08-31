package shop.ecoswapshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.ecoswapshop.domain.Favorite;
import shop.ecoswapshop.domain.Member;
import shop.ecoswapshop.domain.Product;
import shop.ecoswapshop.repository.FavoriteRepository;
import shop.ecoswapshop.repository.MemberRepository;
import shop.ecoswapshop.repository.ProductRepository;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void addFavorite(Long memberId, Long productId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member Not Found"));
        Product product = productRepository
                .findById(productId).orElseThrow(() -> new IllegalArgumentException("Product Not Found"));
        Favorite existingFavorite = favoriteRepository.findByMemberIdAndProductId(memberId, productId);
        if (existingFavorite != null) {
            throw new IllegalArgumentException("Already favorite");
        }

        Favorite favorite = new Favorite();
        favorite.setMember(member);
        favorite.setProduct(product);
        favoriteRepository.save(favorite);
    }

    public List<Favorite> getFavoritesByMember(Long memberId) {
        return favoriteRepository.findByMemberId(memberId);
    }

    public List<Favorite> getFavoriteBtProduct(Long productId) {
        return favoriteRepository.findByProductId(productId);
    }
}
