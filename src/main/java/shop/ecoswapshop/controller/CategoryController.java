package shop.ecoswapshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import shop.ecoswapshop.domain.Category;
import shop.ecoswapshop.service.CategoryService;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/categories")
    public String list(Model model) {
        List<Category> categories = categoryService.findAll();
        model.addAttribute("categories", categories);
        return "categories/list";
    }

    @GetMapping("/categories/new")
    public String createForm(Model model) {
        model.addAttribute("categoryForm", new Category());
        model.addAttribute("categories", categoryService.findAll()); // 상위 카테고리 선택을 위해
        return "categories/create";
    }

    @PostMapping("/categories/new")
    public String create(@ModelAttribute Category category) {
        categoryService.save(category);
        return "redirect:/categories";
    }

    @GetMapping("/categories/{id}/edit")
    public String editForm(@PathVariable Long id, Model model) {
        Category category = categoryService.findById(id);
        model.addAttribute("categoryForm", category);
        model.addAttribute("categories", categoryService.findAll());
        return "categories/edit";
    }

    @PostMapping("/categories/{id}/edit")
    public String edit(@PathVariable Long id, @ModelAttribute Category category) {
        categoryService.save(category); // 여기서는 간단하게 save로 처리. 실제로는 update 로직이 필요할 수 있음.
        return "redirect:/categories";
    }

    // 삭제 메서드 및 기타 필요한 메서드 추가
}
