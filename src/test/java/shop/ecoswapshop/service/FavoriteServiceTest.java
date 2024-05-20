package shop.ecoswapshop.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import shop.ecoswapshop.domain.Favorite;
import shop.ecoswapshop.domain.Member;
import shop.ecoswapshop.domain.Product;
import shop.ecoswapshop.repository.FavoriteRepository;
import shop.ecoswapshop.repository.MemberRepository;
import shop.ecoswapshop.repository.ProductRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FavoriteServiceTest {

    @Mock
    private FavoriteRepository favoriteRepository;

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private FavoriteService favoriteService;

    @SuppressWarnings("unchecked")
    private <T> Page<T> mockPage() {
        return mock(Page.class);
    }

    @Test
    void toggleFavorite_AddFavorite() {
        // given
        Long memberId = 1L;
        Long productId = 1L;
        Member member = new Member();
        Product product = new Product();

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(favoriteRepository.findByMemberIdAndProductId(memberId, productId)).thenReturn(null);

        // when
        favoriteService.toggleFavorite(memberId, productId);

        // then
        verify(favoriteRepository).save(any(Favorite.class));
    }

    @Test
    void toggleFavorite_RemoveFavorite() {
        // given
        Long memberId = 1L;
        Long productId = 1L;
        Member member = new Member();
        Product product = new Product();
        Favorite favorite = new Favorite();

        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(favoriteRepository.findByMemberIdAndProductId(memberId, productId)).thenReturn(favorite);

        // when
        favoriteService.toggleFavorite(memberId, productId);

        // then
        verify(favoriteRepository).delete(favorite);
    }

    @Test
    void getFavoritesByMember() {
        // given
        Long memberId = 1L;
        int page = 0;
        int pageSize = 10;
        Pageable pageable = PageRequest.of(page, pageSize);
        Page<Favorite> expectedPage = mockPage();

        when(favoriteRepository.findByMemberId(memberId, pageable)).thenReturn(expectedPage);

        // when
        Page<Favorite> resultPage = favoriteService.getFavoritesByMember(memberId, page, pageSize);

        // then
        assertNotNull(resultPage);
        assertEquals(expectedPage, resultPage);
    }
}
