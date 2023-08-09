package shop.ecoswapshop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static jakarta.persistence.CascadeType.ALL;
import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
public class Post {

    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id; // 게시글 아이디

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 작성자 아이디

    private String title; // 게시글 제목

    private String content; // 게시글 내용

    private LocalDateTime creationDate; // 작성일

    @OneToMany(mappedBy = "post", cascade = ALL)
    private List<Comment> commentList = new ArrayList<>();

    // ==연관관계 메서드==
    public void setMember(Member member) {
        this.member=member;
        member.getPostList().add(this);
    }
    public void addComment(Comment comment) {
        commentList.add(comment);
        comment.setPost(this);
    }
}

