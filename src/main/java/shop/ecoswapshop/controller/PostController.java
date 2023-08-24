package shop.ecoswapshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import shop.ecoswapshop.service.PostService;

@Controller
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @GetMapping("/new")
    public String showPostForm(Model model) {
        model.addAttribute("postForm", new PostForm());
        return "post/create";
    }

    @PostMapping("/new")
    public String createPost(@ModelAttribute("postForm") PostForm postForm) {

        return "redirect:/post/new?success";
    }


}
