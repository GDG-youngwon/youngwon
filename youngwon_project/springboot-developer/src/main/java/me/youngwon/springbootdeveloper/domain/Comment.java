package me.youngwon.springbootdeveloper.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 어떤 글에 달린 댓글인지 (Article이랑 연결)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_id", nullable = false)
    private Article article;

    // 댓글 작성자 이름 (지금은 문자열로만)
    @Column(nullable = false)
    private String author;

    // 댓글 내용
    @Column(nullable = false, length = 1000)
    private String content;

    @Builder
    public Comment(Article article, String author, String content) {
        this.article = article;
        this.author = author;
        this.content = content;
    }

    // 댓글 내용 수정할 때 사용
    public void update(String content) {
        this.content = content;
    }
}

