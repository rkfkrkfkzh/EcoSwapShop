package shop.ecoswapshop.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.ecoswapshop.domain.Comment;
import shop.ecoswapshop.repository.CommentRepository;
import shop.ecoswapshop.repository.MemberRepository;
import shop.ecoswapshop.repository.PostRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CommentService {

    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentService(MemberRepository memberRepository, CommentRepository commentRepository, PostRepository postRepository) {
        this.memberRepository = memberRepository;
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    // 댓글 등록
    @Transactional
    public Long registerComment(Comment comment) {
        return commentRepository.save(comment).getId();
    }

    // 특정 게시글에 대한 모든 댓글 조회
    public List<Comment> getCommentsByPostId(Long postId) {
        return commentRepository.findByPostId(postId);
    }

    // 특정 사용자가 작성한 모든 댓글 조회
    public List<Comment> getCommentsByMemberId(Long memberId) {
        return commentRepository.findByMemberId(memberId);
    }

    // 특정 상품에 대한 특정 사용자의 댓글 조회
    public List<Comment> getCommentsByPostIdAndMemberId(Long postId, Long memberId) {
        return commentRepository.findByPostIdAndMemberId(postId, memberId);
    }

    // 댓글 삭제
    @Transactional
    public void deleteCommentById(Long commentId) {
        commentRepository.deleteById(commentId);
    }

    // 전체 댓글 삭제
    @Transactional
    public void deleteAllComments() {
        commentRepository.deleteAll();
    }
}
