package shop.ecoswapshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import shop.ecoswapshop.domain.Member;
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

    // 특정 등급의 회원들 조회
    List<Member> findByType(UserType type);

    // 회원 등록일 기준으로 조회 (특정 날짜 이후의 회원들)
    List<Member> findByRegistrationDateAfter(LocalDateTime registrationDate);

    // 회원 등록일과 등급 기준으로 조회 (특정 날짜 이후, 특정 등급 이상의 회원들)
    List<Member> findByRegistrationDateAfterAndType(LocalDateTime registrationDate, UserType type);

    // 회원 등록일과 등급, 이름으로 복합 조건으로 조회
    List<Member> findByRegistrationDateAfterAndTypeAndFullName(LocalDateTime registrationDate, UserType type, String fullName);

    // 로그인 기능 - 이메일과 비밀번호로 회원 조회
    Optional<Member> findByEmailAndPassword(String email, String password);
}
