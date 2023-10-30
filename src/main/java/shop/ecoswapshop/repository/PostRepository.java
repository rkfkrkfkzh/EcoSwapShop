package shop.ecoswapshop.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.ecoswapshop.domain.Post;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // 특정 사용자가 작성한 모든 게시글 조회
    Page<Post> findByMemberId(Long memberId, Pageable pageable);

    // 게시글 제목에 해당하는 문자열을 포함하는 게시글 조회
    List<Post> findByTitleContaining(String keyword);
    // JPA는 "Content"라는 필드에서 "Containing"이라는 조건(즉, SQL의 LIKE %...%)을
    // 사용하여 입력된 "keyword" 파라미터를 찾는 쿼리를 생성

    // 게시글 내용에 해당하는 문자열을 포함하는 게시글 조회
    List<Post> findByContentContaining(String keyword);

    // 게시글 제목과 내용에 해당하는 문자열을 포함하는 게시글 조회
    Page<Post> findByTitleContainingOrContentContaining(String title, String content, Pageable pageable);

    // 페이징 처리를 위한 메서드
    Page<Post> findAll(Pageable pageable);
}
