package shop.ecoswapshop.service;

import jakarta.validation.constraints.AssertFalse;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import shop.ecoswapshop.domain.Member;
import shop.ecoswapshop.domain.Post;
import shop.ecoswapshop.repository.MemberRepository;
import shop.ecoswapshop.repository.PostRepository;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class PostServiceTest {

    @Autowired
    PostService postService;
    @Autowired
    PostRepository postRepository;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @Rollback(value = false)
    public void 게시글_등록() throws Exception {
        //given
        Member member = new Member();
        member.setFullName("lim");
        memberRepository.save(member);

        Post post = new Post();
        post.setMember(member);
        post.setTitle("게시글1");
        post.setContent("This is a test content");
        post.setCreationDate(LocalDateTime.now());

        //when
        Long createPost = postService.createPost(post);

        //then
        assertEquals(post.getId(), createPost);
    }

    @Test
    public void 특정_사용자_게시글_조회() throws Exception {
        //given
        Member member = new Member();
        member.setFullName("lim");
        memberRepository.save(member);

        Post post = new Post();
        post.setMember(member);
        post.setTitle("게시글1");
        post.setContent("This is a test content");
        post.setCreationDate(LocalDateTime.now());
        postService.createPost(post);

        //when
        List<Post> postsByMemberId = postService.getPostsByMemberId(member.getId());

        //then
        assertFalse(postsByMemberId.isEmpty());
        assertEquals(postsByMemberId.get(0).getTitle(), "게시글1");
    }

    @Test
    public void 게시글_검색() throws Exception {
        //given
        String keyword = "테스트1";
        Post post = new Post();
        post.setTitle(keyword);
        post.setContent(keyword);
        Long createPost = postService.createPost(post);

        //when
        List<Post> postsByTitle = postService.getPostsByTitle(keyword);
        List<Post> postsByContent = postService.getPostsByContent(keyword);

        //then
        assertFalse(postsByTitle.isEmpty());
        assertFalse(postsByContent.isEmpty());
        assertEquals(postsByTitle.get(0).getTitle(), "테스트1");
        assertEquals(postsByContent.get(0).getContent(), "테스트1");
    }

    @Test
    @Rollback(value = false)
    public void 게시글_삭제() throws Exception {
        //given
        Member member = new Member();
        member.setFullName("lim");
        memberRepository.save(member);

        Post post = new Post();
        post.setMember(member);
        post.setTitle("게시글1");
        post.setContent("This is a test content");
        post.setCreationDate(LocalDateTime.now());
        Long post1 = postService.createPost(post);

        //when
        assertNotNull(post1);
        postService.deletePost(post1);

        //then
        List<Post> postsByMemberId = postService.getPostsByMemberId(member.getId());
        assertTrue(postsByMemberId.isEmpty());
    }

}