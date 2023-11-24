package shop.ecoswapshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.ecoswapshop.domain.Favorite;
import shop.ecoswapshop.domain.Member;
import shop.ecoswapshop.domain.Product;
import shop.ecoswapshop.repository.FavoriteRepository;
import shop.ecoswapshop.repository.MemberRepository;
import shop.ecoswapshop.repository.ProductRepository;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;

    @Transactional
    public void toggleFavorite(Long memberId, Long productId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("Member Not Found"));
        Product product = productRepository
                .findById(productId).orElseThrow(() -> new IllegalArgumentException("Product Not Found"));
        Favorite existingFavorite = favoriteRepository.findByMemberIdAndProductId(memberId, productId);
        if (existingFavorite != null) {
            favoriteRepository.delete(existingFavorite);
        } else {
            Favorite favorite = new Favorite();
            favorite.setMember(member);
            favorite.setProduct(product);
            favoriteRepository.save(favorite);
        }
    }

    public Page<Favorite> getFavoritesByMember(Long memberId, int page, int pageSize) {
        PageRequest pageable = PageRequest.of(page, pageSize);
        return favoriteRepository.findByMemberId(memberId, pageable);
    }
}
