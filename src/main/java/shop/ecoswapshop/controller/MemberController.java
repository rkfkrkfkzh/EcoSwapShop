package shop.ecoswapshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import shop.ecoswapshop.domain.Address;
import shop.ecoswapshop.domain.Member;
import shop.ecoswapshop.service.MemberService;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "members/createMemberForm";
        }
        Member member = new Member();
        member.setUsername(form.getUsername());
        member.setPassword(form.getPassword());
        member.setEmail(form.getEmail());
        member.setFullName(form.getFullName());
        member.setPhoneNumber(form.getPhoneNumber());
        member.setAddress(new Address(form.getCity(), form.getStreet(), form.getZipcode()));

        memberService.registerMember(member);
        return "redirect:/";
    }

    @GetMapping("/members")
    public String list(Model model) {
        List<Member> Members = memberService.findAllMembers();
        model.addAttribute("members", Members);
        return "members/memberList";
    }

    @GetMapping("/members/detail/{memberId}")
    public String detail(@PathVariable Long memberId, Model model) {
        Optional<Member> member = memberService.findMemberById(memberId);
        model.addAttribute("member", member);
        return "members/login";
    }

    @DeleteMapping("/members/{memberId}")
    public String delete(@PathVariable Long memberId) {
        memberService.deleteMemberById(memberId);
        return "redirect:/members";
    }
    // 아이디 중복 검사
    @GetMapping("/members/exists/{username}")
    public ResponseEntity<Boolean> checkUsernameExists(@PathVariable String username) {
        boolean exists = memberService.existsByUsername(username);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }
    //로그인, 수정, 이메일, 전화번호, 등급 등으로 회원 검색과 같은 추가 기능을 구현
}
