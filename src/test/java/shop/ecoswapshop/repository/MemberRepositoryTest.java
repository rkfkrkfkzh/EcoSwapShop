package shop.ecoswapshop.repository;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import shop.ecoswapshop.domain.Member;
import shop.ecoswapshop.domain.UserType;

@RunWith(SpringRunner.class)
@SpringBootTest
@Rollback(value = false)
public class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void testMember() throws Exception {
        //given
        Member member = new Member();
        member.setUsername("memberA");
        member.setPassword("111");
        member.setType(UserType.ROLE_ADMIN);
        member.setEmail("123123@saf");
        member.setFullName("ieieidj");

        //when
        memberRepository.save(member);
        Member findMember = memberRepository.findOne(member.getId());

        //then
        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
        Assertions.assertThat(findMember).isEqualTo(member);
        System.out.println("(findMember==member) = " + (findMember==member));
    }
}