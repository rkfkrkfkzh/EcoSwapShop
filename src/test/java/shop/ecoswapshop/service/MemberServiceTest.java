package shop.ecoswapshop.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import shop.ecoswapshop.domain.Address;
import shop.ecoswapshop.domain.Member;
import shop.ecoswapshop.domain.MemberStatus;
import shop.ecoswapshop.domain.UserType;
import shop.ecoswapshop.repository.MemberRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberServiceTest {

    @Mock
    private MemberRepository memberRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private MemberService memberService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void registerMember() {
        // given
        Member member = new Member();
        member.setId(1L);
        member.setUsername("oldUsername");
        member.setPassword("password");
        member.setEmail("email@example.com");
        member.setFullName("Full Name");
        member.setPhoneNumber("1234567890");
        member.setAddress(new Address("su","susu","123"));
        member.setType(UserType.USER);

        when(memberRepository.save(any(Member.class))).thenReturn(member);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");

        // when
        Long registeredId = memberService.registerMember(member);

        // then
        assertNotNull(registeredId);
        verify(memberRepository).save(any(Member.class));
        verify(passwordEncoder).encode(anyString());
    }

    @Test
    public void findMemberById() {
        // given
        Member member = new Member();
        member.setId(1L);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        // when
        Optional<Member> found = memberService.findMemberById(1L);

        // then
        assertTrue(found.isPresent());
        assertEquals(member.getId(), found.get().getId());
    }

    @Test
    public void findAllMembers() {
        // given
        List<Member> members = new ArrayList<>();
        members.add(new Member());
        members.add(new Member());

        when(memberRepository.findAll()).thenReturn(members);

        // when
        List<Member> foundMembers = memberService.findAllMembers();

        // then
        assertNotNull(foundMembers);
        assertEquals(2, foundMembers.size());
        verify(memberRepository).findAll();
    }

    @Test
    public void updateMember() {
        // given
        Member member = new Member();
        member.setId(1L);
        member.setUsername("oldUsername");
        member.setPassword("password");
        member.setEmail("email@example.com");
        member.setFullName("Full Name");
        member.setPhoneNumber("1234567890");
        member.setAddress(new Address("su","susu","123"));
        member.setType(UserType.USER);
        member.setRegistrationDate(LocalDateTime.now());

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));
        when(memberRepository.save(any(Member.class))).thenReturn(member);

        // when
        memberService.updateMember(1L, "newUsername", "newPassword", "newEmail", "New Full Name", "newPhoneNumber", new Address("uuu","sss","122"));

        //then
        assertEquals("newUsername", member.getUsername());

        verify(memberRepository).save(any(Member.class));
    }

    @Test
    public void deleteMemberById() {
        // given
        doNothing().when(memberRepository).deleteById(1L);

        // when
        memberService.deleteMemberById(1L);

        // then
        verify(memberRepository, times(1)).deleteById(1L);
    }

    @Test
    public void existsByUsername() {
        // given
        when(memberRepository.existsByUsername("username")).thenReturn(true);

        // when
        boolean exists = memberService.existsByUsername("username");

        // then
        assertTrue(exists);
    }

    @Test
    public void deactivateMember() {
        // given
        Member member = new Member();
        member.setId(1L);
        member.setStatus(MemberStatus.ACTIVE);

        when(memberRepository.findById(1L)).thenReturn(Optional.of(member));

        // when
        memberService.deactivateMember(1L);

        // then
        assertEquals(MemberStatus.DEACTIVATED, member.getStatus());
    }

    @Test
    public void validatePassword() {
        // given
        Member member = new Member();
        member.setPassword("encodedPassword");

        when(passwordEncoder.matches("rawPassword", "encodedPassword")).thenReturn(true);

        // when
        boolean isValid = memberService.validatePassword(member, "rawPassword");

        // then
        assertTrue(isValid);
    }

    @Test
    public void resetPassword() {
        // given
        Member member = new Member();
        member.setId(1L);
        member.setEmail("user@example.com");

        when(memberRepository.findByUsername("username")).thenReturn(member);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        doNothing().when(emailService).sendEmail(anyString(), anyString(), anyString());

        // when
        memberService.resetPassword("username", "user@example.com");

        // then
        assertNotNull(member.getPassword());
        verify(emailService, times(1)).sendEmail(anyString(), anyString(), anyString());
    }
}
