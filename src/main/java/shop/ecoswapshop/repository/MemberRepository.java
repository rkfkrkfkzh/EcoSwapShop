package shop.ecoswapshop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import shop.ecoswapshop.domain.Member;
import shop.ecoswapshop.domain.UserType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
@Transactional(readOnly = true) //조회만 하고 수정은 하지 않는다는 의미
@RequiredArgsConstructor
public class MemberRepository {

    private final EntityManager em;

    // 회원 가입
    public void save(Member member) {
        em.persist(member);
    }

    // 회원 번호로 찾기
    public Member findById(Long id) {
        return em.find(Member.class, id);
    }

    // 회원 전체 조회
    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    // 회원 이름으로 조회
    public List<Member> findByFullName(String fullName) {
        return em.createQuery("select m from Member m where m.fullName = :fullName", Member.class)
                .setParameter("fullName", fullName)
                .getResultList();
    }

    // 회원 이메일로 조회
    public Optional<Member> findByEmail(String email) {
        try {
            Member member = em.createQuery("select m from Member m where m.email = :email", Member.class)
                    .setParameter("email", email)
                    .getSingleResult();
            return Optional.ofNullable(member);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    // 회원 전화번호로 조회
    public Optional<Member> findByPhoneNumber(String phoneNumber) {
        try {
            Member member = em.createQuery("select m from Member m where m.phoneNumber =:phoneNumber", Member.class)
                    .setParameter("phoneNumber", phoneNumber)
                    .getSingleResult();
            return Optional.ofNullable(member);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    // 특정 등급의 회원들 조회
    public List<Member> findByType(UserType type) {
        return em.createQuery("select m from Member m where m.type = :type", Member.class)
                .setParameter("role", type)
                .getResultList();
    }

    // 회원 등록일 기준으로 조회 (특정 날짜 이후의 회원들)
    public List<Member> findByRegistrationDateAfter(LocalDateTime registrationDate) {
        return em.createQuery("select m from Member m where m.registrationDate > :registrationDate", Member.class)
                .setParameter("registrationDate", registrationDate)
                .getResultList();
    }

    // 회원 등록일과 등급 기준으로 조회 (특정 날짜 이후, 특정 등급 이상의 회원들)
    public List<Member> findByRegistrationDateAfterAndType(LocalDateTime registrationDate, UserType type) {
        return em.createQuery("select m from Member m where m.registrationDate > :registrationDate and m.type = :type", Member.class)
                .setParameter("registrationDate", registrationDate)
                .setParameter("type", type)
                .getResultList();
    }

    // 회원 등록일과 등급, 이름으로 복합 조건으로 조회
    public List<Member> findByRegistrationDateAfterAndTypeAndName(LocalDateTime registrationDate, UserType type, String fullName) {
        return em.createQuery("select m from Member m where m.registrationDate > :registrationDate and m.type =:type and m.fullName=:fullName", Member.class)
                .setParameter("registrationDate", registrationDate)
                .setParameter("type", type)
                .setParameter("fullName", fullName)
                .getResultList();
    }

    // 회원 삭제
    public void deleteById(Long id) {
        Member member = em.find(Member.class, id);
        if (member != null) {
            em.remove(member);
        }
    }

    // 로그인 기능 - 이메일과 비밀번호로 회원 조회
    public Optional<Member> findByEmailAndPassword(String email, String password) {
        try {
            Member member = em.createQuery("select m from Member m where m.email=:email and m.password = :password", Member.class)
                    .setParameter("email", email)
                    .setParameter("password", password)
                    .getSingleResult();
            return Optional.ofNullable(member);
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }
}