package me.youngwon.springbootdeveloper.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AddCommentRequest {

    // 댓글 내용만 받기 (작성자는 로그인된 사용자 이름 사용)
    private String content;
}
