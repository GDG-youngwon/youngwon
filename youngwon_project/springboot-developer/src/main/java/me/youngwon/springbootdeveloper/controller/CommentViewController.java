package me.youngwon.springbootdeveloper.controller;

import lombok.RequiredArgsConstructor;
import me.youngwon.springbootdeveloper.dto.AddCommentRequest;
import me.youngwon.springbootdeveloper.service.CommentService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RequiredArgsConstructor
@Controller
public class CommentViewController {

    private final CommentService commentService;

    // 댓글 작성
    @PostMapping("/articles/{articleId}/comments")
    public String addComment(
            @PathVariable Long articleId,
            @RequestParam String content,
            Principal principal
    ) {
        String username = principal.getName();   // ✅ 이제 null 아님
        commentService.save(articleId, new AddCommentRequest(content), username);
        return "redirect:/articles/" + articleId;
    }

    // 댓글 삭제
    @PostMapping("/comments/{commentId}/delete")
    public String deleteComment(
            @PathVariable Long commentId,
            @RequestParam Long articleId
    ) {
        commentService.delete(commentId);
        return "redirect:/articles/" + articleId;
    }
}

