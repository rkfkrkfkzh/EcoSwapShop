package shop.ecoswapshop.service;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import shop.ecoswapshop.domain.Address;
import shop.ecoswapshop.domain.Member;
import shop.ecoswapshop.domain.UserType;
import shop.ecoswapshop.repository.MemberRepository;

import java.time.LocalDateTime;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
    @Rollback(value = false)
    public void 회원가입() throws Exception {
        Member member = new Member();
        member.setUsername("john_doe");
        member.setPassword("password");
        member.setFullName("John Doe");
        member.setEmail("john.doe@example.com");
        member.setPhoneNumber("1234567890");
        member.setAddress(new Address("City", "Street", "12345"));
        member.setType(UserType.TYPE_USER);
        member.setRegistrationDate(LocalDateTime.now());

        //when
        Long memberId = memberService.registerMember(member);

        //then
        Assertions.assertNotNull(memberId);
        Assertions.assertTrue(memberId > 0);

        Member savedMember = memberRepository.findById(memberId);
        Assertions.assertNotNull(savedMember);
        Assertions.assertEquals(member.getUsername(), savedMember.getUsername());
        Assertions.assertEquals(member.getEmail(), savedMember.getEmail());
    }

    @Test
    public void 중복_회원가입() throws Exception {
        //given
        Member member1 = new Member();
        member1.setUsername("user1");
        member1.setPassword("password");
        member1.setFullName("Lim");
        member1.setEmail("Lim.doe@example.com");
        member1.setPhoneNumber("1234567890");
        member1.setAddress(new Address("City", "Street", "12345"));
        member1.setType(UserType.TYPE_USER);
        member1.setRegistrationDate(LocalDateTime.now());

        Member member2 = new Member();
        member2.setUsername("user2");
        member2.setPassword("password");
        member2.setFullName("Lim");
        member2.setEmail("kim.doe@example.com");
        member2.setPhoneNumber("987654321");
        member2.setAddress(new Address("City", "Street", "12345"));
        member2.setType(UserType.TYPE_USER);
        member2.setRegistrationDate(LocalDateTime.now());

        //when
        memberService.registerMember(member1);

        //then
        Assertions.assertThrows(IllegalStateException.class, () -> memberService.registerMember(member2));
    }

    @Test
    public void 회원_삭제() throws Exception {
        //given
        Member member = new Member();
        member.setUsername("user1");
        member.setPassword("password");
        member.setFullName("Lim");
        member.setEmail("Lim.doe@example.com");
        member.setPhoneNumber("1234567890");
        member.setAddress(new Address("City", "Street", "12345"));
        member.setType(UserType.TYPE_USER);
        member.setRegistrationDate(LocalDateTime.now());

        //when
        Long memberId = memberService.registerMember(member);

        //then
        Assertions.assertNotNull(memberId);

        memberService.deleteMemberById(memberId);

        Member deleteMember = memberService.findMemberById(memberId);
        Assertions.assertNull(deleteMember);
    }
}