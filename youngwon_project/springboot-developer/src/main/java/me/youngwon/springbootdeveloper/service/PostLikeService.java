package me.youngwon.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.youngwon.springbootdeveloper.domain.Article;
import me.youngwon.springbootdeveloper.domain.PostLike;
import me.youngwon.springbootdeveloper.domain.User;
import me.youngwon.springbootdeveloper.repository.BlogRepository;
import me.youngwon.springbootdeveloper.repository.PostLikeRepository;
import me.youngwon.springbootdeveloper.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final UserRepository userRepository;
    private final BlogRepository blogRepository;

    // 좋아요 토글 (이미 누른 경우 취소, 안 누른 경우 등록)
    @Transactional
    public void toggleLike(Long userId, Long postId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 userId입니다."));
        Article article = blogRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 postId입니다."));

        postLikeRepository.findByUserAndArticle(user, article)
                .ifPresentOrElse(
                        postLikeRepository::delete, // 이미 눌렀으면 삭제 (좋아요 취소)
                        () -> postLikeRepository.save(PostLike.builder() // 안 눌렀으면 등록
                                .user(user)
                                .article(article)
                                .build()
                        )
                );
    }

    // 해당 게시글의 좋아요 수 반환
    public long countLikes(Long postId) {
        Article article = blogRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 postId입니다."));
        return postLikeRepository.countByArticle(article);
    }

    // 특정 사용자가 해당 게시글에 좋아요를 눌렀는지 여부 확인
    public boolean isLiked(Long userId, Long postId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 userId입니다."));
        Article article = blogRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 postId입니다."));
        return postLikeRepository.findByUserAndArticle(user,article).isPresent();
    }
}
