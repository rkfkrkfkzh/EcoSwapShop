package shop.ecoswapshop.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import shop.ecoswapshop.domain.Post;
import shop.ecoswapshop.repository.MemberRepository;
import shop.ecoswapshop.repository.PostRepository;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PostService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;

    public PostService(MemberRepository memberRepository, PostRepository postRepository) {
        this.memberRepository = memberRepository;
        this.postRepository = postRepository;
    }

    // 게시글 작성
    @Transactional
    public Long createPost(Post post) {
        return postRepository.save(post).getId();
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
}
