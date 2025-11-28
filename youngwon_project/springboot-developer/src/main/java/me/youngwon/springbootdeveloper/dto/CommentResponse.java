package me.youngwon.springbootdeveloper.dto;

import lombok.Getter;
import me.youngwon.springbootdeveloper.domain.Comment;

@Getter
public class CommentResponse {

    private final Long id;
    private final String author;
    private final String content;

    public CommentResponse(Comment comment) {
        this.id = comment.getId();
        this.author = comment.getAuthor();
        this.content = comment.getContent();
    }
}