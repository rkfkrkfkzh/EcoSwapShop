package shop.ecoswapshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.ecoswapshop.domain.Member;
import shop.ecoswapshop.domain.MemberStatus;
import shop.ecoswapshop.domain.UserType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // 회원 아이디로 조회
    Member findByUsername(String userName);

    // 회원 이름으로 조회
    List<Member> findByFullName(String fullName);

    // 회원 이메일로 조회
    Optional<Member> findByEmail(String email);

    // 회원 전화번호로 조회
    Optional<Member> findByPhoneNumber(String phoneNumber);

    // 아이디 중복체크
    boolean existsByUsername(String username);
}
