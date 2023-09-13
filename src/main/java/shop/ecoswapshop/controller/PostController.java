package shop.ecoswapshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import shop.ecoswapshop.domain.Member;
import shop.ecoswapshop.domain.Post;
import shop.ecoswapshop.service.MemberService;
import shop.ecoswapshop.service.PostService;

import java.time.LocalDateTime;
import java.util.Optional;

@Controller
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final MemberService memberService;

    private Optional<Long> getLoggedInMemberId() {
        return memberService.findLoggedInMemberId();
    }

    private Member getLoggedInMember() {
        return getLoggedInMemberId()
                .map(memberService::findMemberById)
                .orElseThrow(() -> new RuntimeException("Logged in member not found"))
                .orElseThrow(() -> new RuntimeException("Member Not Found"));
    }

    private boolean isUserAuthorized(Long memberId) {
        return getLoggedInMemberId().isPresent() && getLoggedInMemberId().get().equals(memberId);
    }

    @GetMapping
    public String showPostList(@RequestParam(defaultValue = "0") int page, Model model) {
        Page<Post> pagedPosts = postService.getPagedPosts(page, 8);
        model.addAttribute("pagedPosts", pagedPosts);
        getLoggedInMemberId().ifPresent(memberId -> model.addAttribute("loggedInMemberId", memberId));
        return "posts/postList";
    }

    // 등록
    @GetMapping("/new")
    public String showPostForm(Model model) {
        Member loggedInMember = getLoggedInMember();
        Post post = new Post();
        model.addAttribute("postForm", new PostForm());
        model.addAttribute("post", post);

        return "posts/create";
    }

    @PostMapping("/create")
    public String createPost(@ModelAttribute PostForm postForm) {
        Member loggedInMember = getLoggedInMember();
        Post post = new Post();
        post.setTitle(postForm.getTitle());
        post.setContent(postForm.getContent());
        post.setCreationDate(LocalDateTime.now());
        post.setMember(loggedInMember);

        postService.createPost(post);
        return "redirect:/posts";
    }

    // 수정
    @GetMapping("/edit/{postId}")
    public String showEditPostForm(@PathVariable Long postId, Model model) {
        Optional<Post> post = postService.findPostById(postId);
        if (!post.isPresent()) {
            return "redirect:/error";
        }
        if (!isUserAuthorized(post.get().getMember().getId())) {
            return "redirect:/error";
        }
        model.addAttribute("postForm", post.get());
        return "posts/postEdit";
    }

    @PostMapping("/edit/{postId}")
    public String edit(@PathVariable Long postId, @ModelAttribute PostForm postForm) {
        Optional<Post> postById = postService.findPostById(postId);
        if (!postById.isPresent()) {
            return "redirect:/error";
        }
        Post post = postById.get();
        if (!isUserAuthorized(post.getMember().getId())) {
            return "redirect:/error";
        }

        post.setTitle(postForm.getTitle());
        post.setContent(postForm.getContent());

        postService.updatePost(post);
        return "redirect:/posts/";
    }

    // 상세 페이지
    @GetMapping("/details/{postId}")
    public String showPostDetail(@PathVariable Long postId, Model model) {
        Optional<Post> post = postService.findPostById(postId);
        if (!post.isPresent()) {
            return "redirect:/error";
        }
        model.addAttribute("post", post.get());
        getLoggedInMemberId().ifPresent(memberId -> model.addAttribute("loggedInMemberId", memberId));
        return "posts/postDetails";
    }

    // 삭제
    @GetMapping("delete/{postId}")
    public String delete(@PathVariable Long postId) {
        Optional<Post> postById = postService.findPostById(postId);
        if (!postById.isPresent()) {
            return "redirect:/error";
        }
        Post post = postById.get();
        if (!isUserAuthorized(post.getMember().getId())) {
            return "redirect:/error";
        }
        postService.deletePostById(postId);
        return "redirect:/posts";
    }

    // 댓글 추가
    @PostMapping("/details/{postId}/addComment")
    public String addComment(@PathVariable Long postId, @RequestParam Long memberId, @RequestParam String content, RedirectAttributes redirectAttributes) {
        Member loggedInMember = getLoggedInMember();
        Long commentId = postService.addComment(postId, memberId, content);
        redirectAttributes.addFlashAttribute("successMessage", "댓글이 성공적으로 추가 되었습니다.");
        return "redirect:/posts/details/" + postId;
    }

    // 댓글 수정
    @PostMapping("details/{postId}/comments/{commentId}/edit")
    public String editComment(@PathVariable Long postId, @PathVariable Long commentId, @RequestParam String newContent, RedirectAttributes redirectAttributes) {
        try {
            Member loggedInMember = getLoggedInMember(); // 로그인한 사용자 가져오기
            postService.editComment(postId, commentId, newContent, loggedInMember.getId()); // 댓글 수정 서비스 메서드
            redirectAttributes.addFlashAttribute("successMessage", "댓글이 성공적으로 수정되었습니다");
            return "redirect:/posts/details/" + postId;
        } catch (Exception e) {
            return "redirect:/posts/details/" + postId;
        }
    }

    // 댓글 삭제
    @PostMapping("details/{postId}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable Long postId, @PathVariable Long commentId, RedirectAttributes redirectAttributes) {
        try {
            Member loggedInMember = getLoggedInMember();
            postService.deleteComment(postId, commentId, loggedInMember.getId());
            redirectAttributes.addFlashAttribute("successMessage", "댓글이 성공적으로 삭제되었습니다");
            return "redirect:/posts/details/" + postId;
        } catch (Exception e) {
            return "redirect:/posts/details/" + postId;
        }
    }

    // 대댓글
    @PostMapping("details/{postId}/addReply/{parentId}")
    public String addReply(@PathVariable Long postId, @PathVariable Long parentId, @RequestParam Long memberId, @RequestParam String content, RedirectAttributes redirectAttributes) {
        Long replyId = postService.addReply(postId, parentId, memberId, content);
        redirectAttributes.addFlashAttribute("successMessage", "대댓글이 성공적으로 추가되었습니다");
        return "redirect:/posts/details/" + postId;

    }
}
