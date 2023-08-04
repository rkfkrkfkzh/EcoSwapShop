package shop.ecoswapshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.ecoswapshop.domain.Comment;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 특정 게시글에 대한 모든 댓글 조회
    List<Comment> findByPostId(Long postId);

    // 특정 사용자가 작성한 모든 댓글 조회
    List<Comment> findByMemberId(Long memberId);

    // 특정 상품에 대한 특정 사용자의 댓글 조회
    List<Comment> findByPostIdAndMemberId(Long postId, Long memberId);
}
