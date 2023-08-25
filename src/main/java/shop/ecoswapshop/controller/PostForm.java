package shop.ecoswapshop.controller;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PostForm {
    private String title;
    private String content;
    private LocalDateTime creationDate;
}
