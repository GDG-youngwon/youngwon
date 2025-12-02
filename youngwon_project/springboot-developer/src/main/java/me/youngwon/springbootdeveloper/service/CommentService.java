package me.youngwon.springbootdeveloper.service;

import lombok.RequiredArgsConstructor;
import me.youngwon.springbootdeveloper.domain.Article;
import me.youngwon.springbootdeveloper.domain.Comment;
import me.youngwon.springbootdeveloper.dto.AddCommentRequest;
import me.youngwon.springbootdeveloper.dto.CommentResponse;
import me.youngwon.springbootdeveloper.repository.BlogRepository;
import me.youngwon.springbootdeveloper.repository.CommentRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final BlogRepository blogRepository;  // Article 조회용

    // 댓글 저장
    public Comment save(Long articleId, AddCommentRequest request, String authorName) {
        Article article = blogRepository.findById(articleId)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + articleId));

        Comment comment = Comment.builder()
                .article(article)
                .author(authorName)
                .content(request.getContent())
                .build();

        return commentRepository.save(comment);
    }

    // 댓글 목록 조회
    public List<CommentResponse> findByArticleId(Long articleId) {
        return commentRepository.findByArticleIdOrderByIdAsc(articleId)
                .stream()
                .map(CommentResponse::new)
                .toList();
    }

    // 댓글 삭제 (본인만)
    public void delete(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("not found: " + commentId));

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        if (!comment.getAuthor().equals(username)) {
            throw new IllegalArgumentException("not authorized");
        }

        commentRepository.delete(comment);
    }
}

