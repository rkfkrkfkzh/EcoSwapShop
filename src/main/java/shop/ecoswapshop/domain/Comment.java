package shop.ecoswapshop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id; // 댓글 아이디

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "member_id")
    private Member member; // 작성자 아이디

    private String content; // 댓글 내용

    private LocalDateTime creationDate; // 작성일

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "post_id")
    private Post post; // 해당 게시글 아이디

    // ==연관관계 메서드==
    public void setMember(Member member) {
        if (this.member != null) {
            this.member.getCommentList().remove(this);
        }
        this.member = member;
        if (this.member != null) {
            member.getCommentList().add(this);
        }
    }

    public void setPost(Post post) {
        this.post = post;
        post.getCommentList().add(this);
    }
}
