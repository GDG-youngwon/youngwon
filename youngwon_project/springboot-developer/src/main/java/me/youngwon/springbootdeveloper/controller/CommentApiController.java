package me.youngwon.springbootdeveloper.controller;

import lombok.RequiredArgsConstructor;
import me.youngwon.springbootdeveloper.domain.Comment;
import me.youngwon.springbootdeveloper.dto.AddCommentRequest;
import me.youngwon.springbootdeveloper.dto.CommentResponse;
import me.youngwon.springbootdeveloper.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RequiredArgsConstructor
@RestController
public class CommentApiController {

    private final CommentService commentService;

    // 댓글 등록
    @PostMapping("/api/articles/{articleId}/comments")
    public ResponseEntity<CommentResponse> addComment(
            @PathVariable Long articleId,
            @RequestBody AddCommentRequest request,
            Principal principal
    ) {
        String username = principal.getName(); // 로그인한 사용자 이름
        Comment saved = commentService.save(articleId, request, username);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new CommentResponse(saved));
    }

    // 댓글 목록 조회
    @GetMapping("/api/articles/{articleId}/comments")
    public ResponseEntity<List<CommentResponse>> getComments(
            @PathVariable Long articleId
    ) {
        List<CommentResponse> comments = commentService.findByArticleId(articleId);
        return ResponseEntity.ok(comments);
    }

    // 댓글 삭제
    @DeleteMapping("/api/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId
    ) {
        commentService.delete(commentId);
        return ResponseEntity.ok().build();
    }
}
