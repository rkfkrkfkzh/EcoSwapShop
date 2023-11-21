package shop.ecoswapshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.ecoswapshop.domain.Comment;
import shop.ecoswapshop.domain.Member;
import shop.ecoswapshop.domain.Post;
import shop.ecoswapshop.exception.NotFoundException;
import shop.ecoswapshop.repository.CommentRepository;
import shop.ecoswapshop.repository.MemberRepository;
import shop.ecoswapshop.repository.PostRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    // 게시글 작성
    @Transactional
    public Long createPost(Post post) {
        return postRepository.save(post).getId();
    }

    // 특정 게시글 조회
    public Optional<Post> findPostById(Long postId) {
        return postRepository.findById(postId);
    }

    // 게시글 삭제
    @Transactional
    public void deletePostById(Long postId) {
        postRepository.deleteById(postId);
    }

    @Transactional
    public void updatePost(Post post) {
        Optional<Post> optionalPost = postRepository.findById(post.getId());

        if (optionalPost.isPresent()) {
            Post existingPost = optionalPost.get();

            existingPost.setTitle(post.getTitle());
            existingPost.setContent(post.getContent());

            postRepository.save(existingPost);
        } else {
            throw new NotFoundException("Post with id " + post.getId());
        }
    }

    // 댓글
    @Transactional
    public Long addComment(Long postId, Long memberId, String content) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new NotFoundException("Invalid post Id:" + postId));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new NotFoundException("Invalid member Id:" + memberId));

        Comment comment = new Comment();
        comment.setMember(member);
        comment.setPost(post);
        comment.setContent(content);
        comment.setCreationDate(LocalDateTime.now());

        Comment saveComment = commentRepository.save(comment);

        return saveComment.getId();
    }

    // 댓글 수정
    @Transactional
    public void editComment(Long postId, Long commentId, String newContent, Long memberId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NoSuchElementException("comment not found"));
        if (!comment.getMember().getId().equals(memberId)) {
            throw new AccessDeniedException("You are not authorized to edit this comment");
        }
        comment.setContent(newContent);
        commentRepository.save(comment);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long postId, Long commentId, Long memberId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new NoSuchElementException("comment not found"));

        if (!comment.getMember().getId().equals(memberId)) {
            throw new AccessDeniedException("You are not authorized to delete this comment");
        }
        commentRepository.delete(comment);
    }

    // 대댓글
    @Transactional
    public Long addReply(Long postId, Long parentId, Long memberId, String content) {
        Post post = findPostById(postId).orElseThrow(() -> new RuntimeException("Post not found"));
        Comment parentComment = commentRepository.findById(parentId).orElseThrow(() -> new RuntimeException("Parent comment not found"));
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new RuntimeException("Member not found"));

        Comment reply = new Comment();
        reply.setContent(content);
        reply.setCreationDate(LocalDateTime.now());
        reply.setMember(member);
        reply.setParentComment(parentComment);

        parentComment.getChildComments().add(reply);
        post.getCommentList().add(reply);

        commentRepository.save(reply);

        return reply.getId();
    }

    // 대댓글 수정
    @Transactional
    public void editReply(Long postId, Long commentId, String newContent, Long memberId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("reply not found"));
        Long parentId = comment.getParentComment().getId();  // 부모 댓글의 ID 가져오기
        if (!comment.getMember().getId().equals(memberId)) {
            throw new AccessDeniedException("You are not authorized to edit this reply");
        }
        comment.setContent(newContent);
        commentRepository.save(comment);
    }

    // 대댓글 삭제
    @Transactional
    public void deleteReply(Long postId, Long commentId, Long memberId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new RuntimeException("reply not found"));
        Long parentId = comment.getParentComment().getId();  // 부모 댓글의 ID 가져오기
        if (!comment.getMember().getId().equals(memberId)) {
            throw new AccessDeniedException("You are not authorized to delete this reply");
        }
        commentRepository.delete(comment);
    }

    // 검색 기능
    public Page<Post> searchPosts(String keyword, Pageable pageable) {
        return postRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable);
    }

    // 페이징 처리
    public Page<Post> getPagedPosts(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return postRepository.findAll(pageable);
    }

    public Page<Post> getMyPosts(Long memberId, int page, int size) {
        return postRepository.findByMemberId(memberId, PageRequest.of(page, size));
    }
}
