package me.youngwon.springbootdeveloper.repository;

import me.youngwon.springbootdeveloper.domain.Article;
import me.youngwon.springbootdeveloper.domain.PostLike;
import me.youngwon.springbootdeveloper.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    // 특정 유저가 특정 게시글에 좋아요를 눌렀는지 여부 조회 (중복 방지용)
    Optional<PostLike> findByUserAndArticle(User user, Article article);

    // 특정 게시글에 눌린 좋아요 개수 카운트
    long countByArticle(Article article);
}
