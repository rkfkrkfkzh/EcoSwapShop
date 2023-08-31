package shop.ecoswapshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import shop.ecoswapshop.domain.Member;
import shop.ecoswapshop.domain.Post;
import shop.ecoswapshop.service.MemberService;
import shop.ecoswapshop.service.PostService;

import java.time.LocalDateTime;
import java.util.List;
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
    public String showPostList(@RequestParam(defaultValue = "0")int page, Model model) {
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
}
