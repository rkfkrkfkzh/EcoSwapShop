package shop.ecoswapshop.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import shop.ecoswapshop.service.MemberService;

@ControllerAdvice
public class GlobalControllerAdvice {

    private final MemberService memberService;

    public GlobalControllerAdvice(MemberService memberService) {
        this.memberService = memberService;
    }

    @ModelAttribute
    public void addAttributes(Model model) {
        memberService.findLoggedInMemberId().ifPresent(memberId -> model.addAttribute("loggedInMemberId", memberId));
    }
}
