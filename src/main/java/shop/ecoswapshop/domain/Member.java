package shop.ecoswapshop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(name = "username") // 아이디
    private String username;

    @Column(name = "password")  // 비밀번호
    private String password;

    @Column(name = "email") // 이메일
    private String email;

    @Column(name = "full_name") // 이름
    private String fullName;

    @Column(name = "phone_number") // 핸드폰번호
    private String phoneNumber;

    @Embedded
    private Address address; // 주소

    @Enumerated(EnumType.STRING)
    private UserRole role; // 회원 타입

    private LocalDateTime registrationDate; // 등록일

    @OneToMany(mappedBy = "member")
    private List<Product> productList= new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<Post> postList = new ArrayList<>();

    // 기타 필드들 (옵션에 따라 생년월일, 프로필 사진 등 추가 가능)

}
