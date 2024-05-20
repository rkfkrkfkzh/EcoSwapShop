package shop.ecoswapshop.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import shop.ecoswapshop.domain.Comment;
import shop.ecoswapshop.domain.Member;
import shop.ecoswapshop.domain.Post;
import shop.ecoswapshop.repository.CommentRepository;
import shop.ecoswapshop.repository.MemberRepository;
import shop.ecoswapshop.repository.PostRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private MemberRepository memberRepository;

    @InjectMocks
    private PostService postService;


    @SuppressWarnings("unchecked")
    private <T> Page<T> mockPage() {
        return mock(Page.class);
    }

    @Test
    void createPost() {
        // Given
        Post post = new Post();
        post.setId(1L);
        when(postRepository.save(any(Post.class))).thenReturn(post);

        // When
        Long postId = postService.createPost(post);

        // Then
        assertNotNull(postId);
        assertEquals(1L, postId);
    }

    @Test
    void findPostById() {
            // Given
            Long postId = 1L;
            Post post = new Post();
            post.setId(postId);
            when(postRepository.findById(postId)).thenReturn(Optional.of(post));

            // When
            Optional<Post> foundPost = postService.findPostById(postId);

            // Then
            assertTrue(foundPost.isPresent());
            assertEquals(postId, foundPost.get().getId());
    }

    @Test
    void deletePostById() {
        // Given
        Long postId = 1L;
        doNothing().when(postRepository).deleteById(postId);

        // When
        postService.deletePostById(postId);

        // Then
        verify(postRepository, times(1)).deleteById(postId);
    }

    @Test
    void updatePost() {
        // Given
        Long postId = 1L;
        Post existingPost = new Post();
        existingPost.setId(postId);
        when(postRepository.findById(postId)).thenReturn(Optional.of(existingPost));

        Post updatedPost = new Post();
        updatedPost.setId(postId);
        updatedPost.setTitle("Updated Title");
        when(postRepository.findById(postId)).thenReturn(Optional.of(updatedPost));
        when(postRepository.save(updatedPost)).thenReturn(updatedPost);

        // When
        postService.updatePost(updatedPost);

        // Then
        verify(postRepository).save(updatedPost);
        assertEquals("Updated Title", updatedPost.getTitle());
    }
    @Test
    void addComment() {
        // Given
        Long postId = 1L;
        Long memberId = 1L;
        String content = "Test Comment";

        Member member = new Member();
        member.setId(memberId);
        Comment comment = new Comment();
        comment.setId(1L);
        comment.setMember(member);

        when(commentRepository.save(any(Comment.class))).thenReturn(comment);
        when(postRepository.findById(postId)).thenReturn(Optional.of(new Post()));
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(new Member()));


        // When
        Long commentId = postService.addComment(postId, memberId, content);

        // Then
        assertNotNull(commentId);
        assertEquals(1L, commentId);
    }

    @Test
    void editComment() {
        // Given
        Long postId = 1L;
        Long commentId = 1L;
        Long memberId = 1L;
        String newContent = "Updated Content";

        Member member = new Member();
        member.setId(memberId);
        Comment existingComment = new Comment();
        existingComment.setId(commentId);
        existingComment.setContent("Original Content");
        existingComment.setMember(member);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(existingComment));

        // When
        postService.editComment(postId, commentId, newContent, memberId);

        // Then
        verify(commentRepository).save(existingComment);
        assertEquals(newContent, existingComment.getContent());
    }

    @Test
    void deleteComment() {
        // Given
        Long postId = 1L;
        Long commentId = 1L;
        Long memberId = 1L;
        Member member = new Member();
        member.setId(memberId);
        Comment comment = new Comment();
        comment.setId(commentId);
        comment.setMember(member);
        when(commentRepository.findById(commentId)).thenReturn(Optional.of(comment));

        // When
        postService.deleteComment(postId, commentId, memberId);

        // Then
        verify(commentRepository).delete(comment);
    }
    @Test
    void addReply() {
        // Given
        Long postId = 1L;
        Long parentId = 1L;
        Long memberId = 1L;
        String content = "Reply Content";

        Comment parentComment = new Comment();
        parentComment.setId(parentId);

        Member member = new Member();
        member.setId(memberId);

        when(commentRepository.findById(parentId)).thenReturn(Optional.of(parentComment));
        when(memberRepository.findById(memberId)).thenReturn(Optional.of(member));
        when(postService.findPostById(postId)).thenReturn(Optional.of(new Post()));
        when(commentRepository.save(any(Comment.class))).thenAnswer(invocation -> {
            Comment savedComment = invocation.getArgument(0);
            savedComment.setId(2L);
            return savedComment;
        });

        // When
        Long replyId = postService.addReply(postId, parentId, memberId, content);

        // Then
        assertNotNull(replyId);
        assertEquals(2L, replyId);
    }


    @Test
    void editReply() {
        // Given
        Long postId = 1L;
        Long replyId = 2L;
        Long memberId = 1L;
        String newContent = "Updated Reply";

        Member member = new Member();
        member.setId(memberId);
        Comment parentComment = new Comment();
        parentComment.setId(1L);
        Comment reply = new Comment();
        reply.setId(replyId);
        reply.setMember(member);
        reply.setParentComment(parentComment);
        reply.setContent("Original Reply");
        when(commentRepository.findById(replyId)).thenReturn(Optional.of(reply));

        // When
        postService.editReply(postId, replyId, newContent, memberId);

        // Then
        verify(commentRepository).save(reply);
        assertEquals(newContent, reply.getContent());
    }

    @Test
    void deleteReply() {
        // Given
        Long postId = 1L;
        Long replyId = 2L;
        Long memberId = 1L;
        Member member = new Member();
        member.setId(memberId);
        Comment reply = new Comment();
        reply.setId(replyId);
        reply.setParentComment(reply);
        reply.setMember(member);
        when(commentRepository.findById(replyId)).thenReturn(Optional.of(reply));

        // When
        postService.deleteReply(postId, replyId, memberId);

        // Then
        verify(commentRepository).delete(reply);
    }
    @Test
    void searchPosts() {
        // Given
        String keyword = "Test";
        Pageable pageable = PageRequest.of(0, 10);
        Page<Post> expectedPage = mockPage(); // 가상의 Page 객체 생성
        when(postRepository.findByTitleContainingOrContentContaining(keyword, keyword, pageable)).thenReturn(expectedPage);

        // When
        Page<Post> resultPage = postService.searchPosts(keyword, pageable);

        // Then
        assertNotNull(resultPage);
        assertEquals(expectedPage, resultPage);
    }


    @Test
    void getPagedPosts() {
        // Given
        int page = 0;
        int size = 10;
//        Pageable pageable = PageRequest.of(page, size);
        Page<Post> expectedPage = mockPage(); // 가상의 Page 객체 생성
        when(postRepository.findAll(any(Pageable.class))).thenReturn(expectedPage);

        // When
        Page<Post> resultPage = postService.getPagedPosts(page, size);

        // Then
        assertNotNull(resultPage);
        assertEquals(expectedPage, resultPage);
    }

    @Test
    void getMyPosts() {
        // Given
        Long memberId = 1L;
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        Page<Post> expectedPage = mockPage(); // 가상의 Page 객체 생성

        when(postRepository.findByMemberId(memberId, pageable)).thenReturn(expectedPage);

        // When
        Page<Post> resultPage = postService.getMyPosts(memberId, page, size);

        // Then
        assertNotNull(resultPage);
        assertEquals(expectedPage, resultPage);
    }
}