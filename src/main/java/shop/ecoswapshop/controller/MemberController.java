package shop.ecoswapshop.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import shop.ecoswapshop.domain.Address;
import shop.ecoswapshop.domain.Member;
import shop.ecoswapshop.service.MemberService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/members")
public class MemberController {

    private final MemberService memberService;

    // 로그인한 사용자의 memberId를 가져옵니다.
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

    @GetMapping("/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "members/createMemberForm";
    }

    @PostMapping("/new")
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
        member.setRegistrationDate(LocalDateTime.now());

        memberService.registerMember(member);
        return "redirect:/";
    }

    @GetMapping
    public String list(Model model) {
        List<Member> members = memberService.findAllMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }

    @GetMapping("/detail/{memberId}")
    public String detail(@PathVariable Long memberId, Model model) {
        Optional<Member> optionalMember = memberService.findMemberById(memberId);
        if (!optionalMember.isPresent()) {
            return "redirect:/error";
        }
        Member member = optionalMember.get();
        model.addAttribute("member", member);

        // 로그인한 사용자의 ID를 모델에 추가합니다.
        getLoggedInMemberId().ifPresent(id -> model.addAttribute("loggedInMemberId", id));

        return "members/memberDetail";
    }

    @DeleteMapping("/{memberId}")
    public String delete(@PathVariable Long memberId) {
        memberService.deleteMemberById(memberId);
        return "redirect:/members";
    }

    // 아이디 중복 검사
    @GetMapping("/exists/{username}")
    public ResponseEntity<Boolean> checkUsernameExists(@PathVariable String username) {
        boolean exists = memberService.existsByUsername(username);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }

    // 회원 정보 수정폼
    @GetMapping("/edit/{memberId}")
    public String editForm(@PathVariable Long memberId, Model model) {
        Optional<Member> memberById = memberService.findMemberById(memberId);
        Optional<Long> loggedInMemberId = memberService.findLoggedInMemberId();
        if (!memberById.isPresent()) {
            return "redirect:/members"; // error page
        }
        Member member = memberById.get();
        MemberForm form = new MemberForm();
        form.setUsername(member.getUsername());
        form.setEmail(member.getEmail());
        form.setFullName(member.getFullName());
        form.setPhoneNumber(member.getPhoneNumber());
        if (member.getAddress() != null) {
            form.setCity(member.getAddress().getCity());
            form.setStreet(member.getAddress().getStreet());
            form.setZipcode(member.getAddress().getZipcode());
        }
        model.addAttribute("loggedInMemberId", memberById);
        model.addAttribute("memberForm", form);
        return "members/editMemberForm";
    }

    // 회원 정보 수정 처리
    @PostMapping("/edit/{memberId}")
    public String edit(@PathVariable Long memberId, @Valid MemberForm form, BindingResult result) {
        if (result.hasErrors() || !form.getPassword().equals(form.getPasswordConfirm())) {
            return "/members/editMemberForm"; // 비밀번호가 다르거나, 양식 유효성 검사요류가 발생할때
        }
        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());
        memberService.updateMember(memberId, form.getUsername(), form.getPassword(), form.getEmail(), form.getFullName(), form.getPhoneNumber(), address);
        return "redirect:/members/detail/" + memberId;
    }

    // 회원 비활성화 화면 요청 처리
    @GetMapping("/deactivate")
    public String deactivateForm() {
        return "members/deactivateConfirm";
    }

    // 회원 비활성화 처리
    @PostMapping("deactivate")
    public String deactivate(@RequestParam String password) {
        Member loggedInMember = getLoggedInMember();

        if (!memberService.validatePassword(loggedInMember, password)) {
            return "redirect:/error";
        }
        memberService.deactivateMember(loggedInMember.getId());
        // 비활성화 처리후 메인 페이지로 리다이렉트
        return "redirect:/";
    }

    // 아이디 찾기 폼
    @GetMapping("/findUsernameForm")
    public String findUsernameForm() {
        return "members/findUsernameForm";
    }

    // 아이디 찾기 처리
    @PostMapping("/findUsername")
    public String findUsername(@RequestParam String email, @RequestParam String phoneNumber, Model model) {
        Optional<String> username = memberService.findUsernameByEmailOrPhoneNumber(email, phoneNumber);
        if (username.isPresent()) {
            model.addAttribute("foundUsername", username.get());
            return "members/usernameFound";
        } else {
            model.addAttribute("errorMessage", "해당 정보와 일치하는 아이디를 찾을 수 없습니다.");
            return "members/findUsernameForm";
        }
    }

    // 비밀번호 찾기 폼
    @GetMapping("/resetPasswordForm")
    public String resetPasswordForm() {
        return "members/resetPasswordForm";
    }

    // 비밀번호 찾기 처리
    @PostMapping("/resetPassword")
    public String resetPassword(@RequestParam String username, @RequestParam String email, Model model) {
        try {
            memberService.resetPassword(username, email);
            return "members/passwordResetSuccess";
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "members/resetPasswordForm";
        }
    }
}
