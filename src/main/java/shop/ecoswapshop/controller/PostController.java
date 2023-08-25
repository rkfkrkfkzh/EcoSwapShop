package shop.ecoswapshop.controller;

import lombok.RequiredArgsConstructor;
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

    @GetMapping
    public String showPostList(Model model) {
        Optional<Long> loggedInMemberId = memberService.findLoggedInMemberId();
        List<Post> posts = postService.findAllPosts();
        model.addAttribute("posts", posts);
        loggedInMemberId.ifPresent(memberId -> model.addAttribute("loggedInMemberId", memberId));
        return "posts/postList";
    }

    // 등록
    @GetMapping("/new")
    public String showPostForm(Model model) {
        Post post = new Post();
        model.addAttribute("postForm", new PostForm());
        model.addAttribute("post", post);

        return "posts/create";
    }

    @PostMapping("/create")
    public String createPost(@ModelAttribute PostForm postForm) {
        Optional<Long> optionalMemberId = memberService.findLoggedInMemberId();

        if (!optionalMemberId.isPresent()) {
            return "redirect:/login";
        }

        Long memberId = optionalMemberId.get();
        Optional<Member> optionalMember = memberService.findMemberById(memberId);

        if (!optionalMember.isPresent()) {
            return "redirect:/error";
        }
        Member member = optionalMember.get();
        Post post = new Post();
        post.setTitle(postForm.getTitle());
        post.setContent(postForm.getContent());
        post.setCreationDate(LocalDateTime.now());
        post.setMember(member);
        postService.createPost(post);
        return "redirect:/posts";
    }

    // 수정
    @GetMapping("/{postId}/edit")
    public String showEditPostForm(@PathVariable Long postId, Model model) {
        Optional<Post> post = postService.findPostById(postId);
        if (!post.isPresent()) {
            return "redirect:/error";
        }
        model.addAttribute("postForm", post.get());
        return "posts/postEdit";
    }

    @PostMapping("/{postId}/edit")
    public String edit(@PathVariable Long postId, @ModelAttribute PostForm postForm) {
        Optional<Post> postById = postService.findPostById(postId);
        if (postById.isPresent()) {
            Post post = postById.get();

            post.setTitle(postForm.getTitle());
            post.setContent(postForm.getContent());

            postService.updatePost(post);
            return "redirect:/posts/";
        }else {
            return "redirect:/posts?error=true";
        }
    }

    // 상세 페이지
    @GetMapping("/details/{postId}")
    public String showPostDetail(@PathVariable Long postId, Model model) {
        Optional<Post> post = postService.findPostById(postId);
        if (!post.isPresent()) {
            return "redirect:/error";
        }
        model.addAttribute("post", post.get());
        return "posts/postDetails";
    }


}
