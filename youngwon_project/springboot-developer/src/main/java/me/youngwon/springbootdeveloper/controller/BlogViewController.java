package me.youngwon.springbootdeveloper.controller;

import lombok.RequiredArgsConstructor;
import me.youngwon.springbootdeveloper.domain.Article;
import me.youngwon.springbootdeveloper.dto.ArticleListViewResponse;
import me.youngwon.springbootdeveloper.dto.ArticleViewResponse;
import me.youngwon.springbootdeveloper.service.BlogService;
import me.leechaeyoung.springbootdeveloper.domain.Article;
import me.leechaeyoung.springbootdeveloper.dto.ArticleListViewResponse;
import me.leechaeyoung.springbootdeveloper.dto.ArticleViewResponse;
import me.leechaeyoung.springbootdeveloper.service.BlogService;
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

import java.util.List;

@RequiredArgsConstructor
@Controller
public class BlogViewController {

    private final BlogService blogService;
    private final CommentService commentService;                 // 추가

    @GetMapping("/articles")
    public String getArticles(Model model) {
        List<ArticleListViewResponse> articles = blogService.findAll().stream()
                .map(ArticleListViewResponse::new)
                .toList();
        model.addAttribute("articles",articles);

        return "articleList";
    }

    @GetMapping("/articles/{id}")
    public String getArticle(@PathVariable Long id, Model model) {
        Article article = blogService.findById(id);

        //  이 글에 달린 댓글 가져오기
        List<CommentResponse> comments = commentService.findByArticleId(id);

        model.addAttribute("article", new ArticleViewResponse(article));
        model.addAttribute("comments", comments);                // 추가

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

        return "newArticle";
    }
}
