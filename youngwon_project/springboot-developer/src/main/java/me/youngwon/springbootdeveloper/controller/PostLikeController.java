package me.youngwon.springbootdeveloper.controller;

import lombok.RequiredArgsConstructor;
import me.youngwon.springbootdeveloper.service.PostLikeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/like")
public class PostLikeController {

    private final PostLikeService postLikeService;

    // 좋아요 등록 또는 취소 (토글)
    // POST /posts/{postId}/like?userId={userId}
    @PostMapping
    public ResponseEntity<Void> toggleLike(
            @PathVariable Long postId,
            @RequestParam Long userId
    ) {
        postLikeService.toggleLike(userId,postId);
        return ResponseEntity.ok().build();
    }

    // 해당 게시글의 좋아요 수 조회
    // GET /posts/{postId}/like/count
    @GetMapping("/count")
    public ResponseEntity<Long> countLikes(@PathVariable Long postId) {
        long count = postLikeService.countLikes(postId);
        return ResponseEntity.ok(count);
    }

    // 사용자가 해당 게시글에 좋아요를 눌렀는지 여부 확인
    // GET /posts/{postId}/like/status?userId={userId}
    @GetMapping("/status")
    public ResponseEntity<Boolean> checkLiked(
            @PathVariable Long postId,
            @RequestParam Long userId
    ) {
        boolean liked = postLikeService.isLiked(userId, postId);
        return ResponseEntity.ok(liked);
    }
}
