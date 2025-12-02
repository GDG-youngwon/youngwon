package me.youngwon.springbootdeveloper.controller;

import lombok.RequiredArgsConstructor;
import me.youngwon.springbootdeveloper.domain.Article;
import me.youngwon.springbootdeveloper.dto.ArticleListViewResponse;
import me.youngwon.springbootdeveloper.dto.ArticleViewResponse;
import me.youngwon.springbootdeveloper.service.BlogService;
import me.youngwon.springbootdeveloper.domain.Article;
import me.youngwon.springbootdeveloper.dto.ArticleListViewResponse;
import me.youngwon.springbootdeveloper.dto.ArticleViewResponse;
import me.youngwon.springbootdeveloper.service.BlogService;
import me.youngwon.springbootdeveloper.domain.Article;
import me.youngwon.springbootdeveloper.dto.ArticleListViewResponse;
import me.youngwon.springbootdeveloper.dto.ArticleViewResponse;
import me.youngwon.springbootdeveloper.dto.CommentResponse;      //  추가
import me.youngwon.springbootdeveloper.service.BlogService;
import me.youngwon.springbootdeveloper.service.CommentService;   //  추가
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import me.youngwon.springbootdeveloper.domain.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import me.youngwon.springbootdeveloper.service.UserService;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BlogViewController {

    private final BlogService blogService;
    private final UserService userService;
    private final CommentService commentService;

    @GetMapping("/articles")
    public String getArticles(Model model) {
        List<ArticleListViewResponse> articles = blogService.findAll().stream()
                .map(ArticleListViewResponse::new)
                .toList();
        model.addAttribute("articles", articles);
        model.addAttribute("userId", resolveUserId());
        return "articleList";
    }

    @GetMapping("/articles/{id}")
    public String getArticle(@PathVariable Long id, Model model) {
        Article article = blogService.findById(id);

        //  이 글에 달린 댓글 가져오기
        List<CommentResponse> comments = commentService.findByArticleId(id);

        model.addAttribute("article", new ArticleViewResponse(article));
        model.addAttribute("userId", resolveUserId());
        model.addAttribute("comments", comments);
        return "article";
    }

    @GetMapping("/new-article")
    public String newArticle(@RequestParam(required = false) Long id, Model model) {
        if (id == null) {
            model.addAttribute("article", new ArticleViewResponse());
        } else {
            Article article = blogService.findById(id);
            model.addAttribute("article", new ArticleViewResponse(article));
        }

        model.addAttribute("userId", resolveUserId());
        return "newArticle";
    }

    // 현재 로그인된 사용자의 userId를 가져옴
    private Long resolveUserId() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object principal = auth.getPrincipal();

        // 1. 직접 만든 User 객체
        if (principal instanceof me.youngwon.springbootdeveloper.domain.User user) {
            return user.getId();

        // 2. OAuth 로그인 사용자
        } else if (principal instanceof DefaultOAuth2User oauthUser) {
            String email = oauthUser.getAttribute("email");
            try {
                return userService.findByEmail(email).getId();
            } catch (Exception e) {
                return -1L;
            }

        // 3. 기본 Spring UserDetails 로그인 사용자
        } else if (principal instanceof UserDetails springUser) {
            String email = springUser.getUsername(); // 기본적으로 username은 email
            try {
                return userService.findByEmail(email).getId();
            } catch (Exception e) {
                return -1L;
            }

        } else {
            return -1L;
        }
    }
}
