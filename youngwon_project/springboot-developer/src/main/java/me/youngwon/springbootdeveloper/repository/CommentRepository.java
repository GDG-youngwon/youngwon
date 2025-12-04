package me.youngwon.springbootdeveloper.repository;

import me.youngwon.springbootdeveloper.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 특정 게시글(article)의 댓글 목록을 시간순으로 조회
    List<Comment> findByArticleIdOrderByIdAsc(Long articleId);
}