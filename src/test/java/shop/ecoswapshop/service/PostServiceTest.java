package shop.ecoswapshop.service;

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
        assertEquals(post.getId(),createPost);

    }
}