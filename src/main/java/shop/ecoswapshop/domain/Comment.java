package shop.ecoswapshop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

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

    // 대댓글 구현
    @ManyToOne
    @JoinColumn(name = "parent_id") // 부모 댓글에 대한 참조
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment") // 대댓글에 대한 참조
    private List<Comment> childComments = new ArrayList<>();

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
