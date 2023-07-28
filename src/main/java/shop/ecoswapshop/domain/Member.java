package shop.ecoswapshop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    @Column(name = "username")
    private String username;

//    @Column(name = "password")
//    private String password;
//
//    @Column(name = "email")
//    private String email;
//
//    @Column(name = "full_name")
//    private String fullName;
//
//    @Column(name = "phone_number")
//    private String phoneNumber;
//
//    @Embedded
//    private Address address;
//
//
//    private UserRole role;

    // 기타 필드들 (옵션에 따라 생년월일, 프로필 사진 등 추가 가능)

}
