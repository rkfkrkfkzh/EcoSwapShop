package shop.ecoswapshop.service;

import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import shop.ecoswapshop.domain.Address;
import shop.ecoswapshop.domain.Member;
import shop.ecoswapshop.domain.UserType;
import shop.ecoswapshop.repository.MemberRepository;

import java.time.LocalDateTime;

import static org.junit.Assert.*;
@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;

    @Test
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

        // Verify that the member is saved in the repository
        Member savedMember = memberRepository.findById(memberId);
        Assertions.assertNotNull(savedMember);
        Assertions.assertEquals(member.getUsername(), savedMember.getUsername());
        Assertions.assertEquals(member.getEmail(), savedMember.getEmail());
    }
}