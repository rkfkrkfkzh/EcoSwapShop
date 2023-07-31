package shop.ecoswapshop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id; // 댓글 아이디

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member; // 작성자 아이디

    private String content; // 댓글 내용

    private LocalDateTime creationDate; // 작성일

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post; // 해당 게시글 아이디

}
