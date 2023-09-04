package shop.ecoswapshop.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    // 모든 게시글 조회
    public List<Post> findAllPosts() {
        return postRepository.findAll();
    }

    // 특정 게시글 조회
    public Optional<Post> findPostById(Long postId) {
        return postRepository.findById(postId);
    }

    // 특정 사용자가 작성한 모든 게시글 조회
    public List<Post> findPostsByMemberId(Long memberId) {
        return postRepository.findByMemberId(memberId);
    }

    // 게시글 제목에 해당하는 문자열을 포함하는 게시글 조회
    public List<Post> findPostsByTitle(String keyword) {
        return postRepository.findByTitleContaining(keyword);
    }

    // 게시글 내용에 해당하는 문자열을 포함하는 게시글 조회
    public List<Post> findPostsByContent(String keyword) {
        return postRepository.findByContentContaining(keyword);
    }

    // 게시글 제목과 내용에 해당하는 문자열을 포함하는 게시글 조회
    public List<Post> findPostsByTitleOrContent(String title, String content) {
        return postRepository.findByTitleContainingOrContentContaining(title, content);
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

    // 페이징 처리
    public Page<Post> getPagedPosts(int page, int pageSize) {
        Pageable pageable = PageRequest.of(page, pageSize);
        return postRepository.findAll(pageable);
    }
}
