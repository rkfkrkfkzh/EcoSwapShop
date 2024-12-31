package shop.ecoswapshop.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    private String provider;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private UserType type = UserType.USER; // 회원 타입(운영자, 고객) 기본값 유저

    private LocalDateTime registrationDate; // 등록일

    @OneToMany(mappedBy = "member", cascade = ALL)
    @Builder.Default
    private List<Product> productList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = ALL)
    @Builder.Default
    private List<Post> postList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = ALL)
    @Builder.Default
    private List<Comment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "member", cascade = ALL)
    @Builder.Default
    private List<Favorite> favorites = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private MemberStatus status = MemberStatus.ACTIVE; // 기본값은 활성 상태

    // 연관관계 메서드
    public void addPost(Post post) {
        this.postList.add(post);
        post.setMember(this);
    }

    public void addComment(Comment comment) {
        this.commentList.add(comment);
        comment.setMember(this);
    }

    // 사용자의 이름이나 이메일을 업데이트하는 메소드
    public Member updateUser(String username, String email) {
        this.username = username;
        this.email = email;

        return this;
    }

}
