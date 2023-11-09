//package shop.ecoswapshop.service;
//
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.annotation.Rollback;
//import org.springframework.test.context.junit4.SpringRunner;
//import org.springframework.transaction.annotation.Transactional;
//import shop.ecoswapshop.domain.Comment;
//import shop.ecoswapshop.domain.Member;
//import shop.ecoswapshop.domain.Post;
//import shop.ecoswapshop.repository.CommentRepository;
//import shop.ecoswapshop.repository.MemberRepository;
//import shop.ecoswapshop.repository.PostRepository;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//
//@SpringBootTest
//@Transactional
//@RunWith(SpringRunner.class)
//public class CommentServiceTest {
//
//    @Autowired
//    private CommentService commentService;
//    @Autowired
//    private CommentRepository commentRepository;
//    @Autowired
//    private MemberRepository memberRepository;
//    @Autowired
//    private PostRepository postRepository;
//
//    @Test
//    @Rollback(value = false)
//    public void 댓글_등록() throws Exception {
//        //given
//        Member member = new Member();
//        member.setFullName("lim");
//        memberRepository.save(member);
//
//        Post post = new Post();
//        post.setTitle("테스트1");
//        postRepository.save(post);
//
//        Comment comment = new Comment();
//        comment.setMember(member);
//        comment.setPost(post);
//        comment.setContent("댓글테스트1");
//        comment.setCreationDate(LocalDateTime.now());
//
//        //when
//        Long aLong = commentService.registerComment(comment);
//
//        //then
//        assertEquals(comment.getId(), aLong);
//    }
//
//    @Test
//    public void 특정_게시글에_대한_모든_댓글_조회() throws Exception {
//        //given
//        Member member = new Member();
//        member.setFullName("lim");
//        memberRepository.save(member);
//
//        Post post = new Post();
//        post.setTitle("테스트1");
//        postRepository.save(post);
//
//        Comment comment = new Comment();
//        comment.setMember(member);
//        comment.setPost(post);
//        comment.setContent("댓글테스트1");
//        comment.setCreationDate(LocalDateTime.now());
//        commentService.registerComment(comment);
//
//        //when
//        List<Comment> commentsByPostId = commentService.getCommentsByPostId(comment.getPost().getId());
//
//        //then
//        assertFalse(commentsByPostId.isEmpty());
//    }
//
//    @Test
//    public void 특정_사용자_댓글_조회() throws Exception {
//        //given
//        Member member = new Member();
//        member.setFullName("lim");
//        memberRepository.save(member);
//
//        Post post = new Post();
//        post.setTitle("테스트1");
//        postRepository.save(post);
//
//        Comment comment = new Comment();
//        comment.setMember(member);
//        comment.setPost(post);
//        comment.setContent("댓글테스트1");
//        comment.setCreationDate(LocalDateTime.now());
//        commentService.registerComment(comment);
//
//        //when
//        List<Comment> comments = commentService.getCommentsByMemberId(comment.getMember().getId());
//
//        //then
//        assertFalse(comments.isEmpty()); // 댓글이 있는지 확인
//    }
//
//    @Test
//    public void 특정_상품_특정_사용자_댓글_조회_테스트() {
//        //given
//        Member member = new Member();
//        member.setFullName("lim");
//        memberRepository.save(member);
//
//        Post post = new Post();
//        post.setTitle("테스트1");
//        postRepository.save(post);
//
//        Comment comment = new Comment();
//        comment.setMember(member);
//        comment.setPost(post);
//        comment.setContent("댓글테스트1");
//        comment.setCreationDate(LocalDateTime.now());
//        commentService.registerComment(comment);
//
//        //when
//        List<Comment> comments = commentService.getCommentsByPostIdAndMemberId(comment.getPost().getId(), comment.getMember().getId());
//
//        //then
//        assertFalse(comments.isEmpty()); // 댓글이 있는지 확인
//    }
//
//    @Test
//    @Rollback(value = false)
//    public void 댓글_삭제() throws Exception {
//        //given
//        Member member = new Member();
//        member.setFullName("lim");
//        memberRepository.save(member);
//
//        Post post = new Post();
//        post.setTitle("테스트1");
//        postRepository.save(post);
//
//        Comment comment = new Comment();
//        comment.setMember(member);
//        comment.setPost(post);
//        comment.setContent("댓글테스트1");
//        comment.setCreationDate(LocalDateTime.now());
//        Long registerComment = commentService.registerComment(comment);
//
//        //when
//        assertNotNull(registerComment);
//        commentService.deleteCommentById(registerComment);
//
//        //then
//        Optional<Comment> byId = commentRepository.findById(registerComment);
//        assertFalse(byId.isPresent());
//
//    }
//}