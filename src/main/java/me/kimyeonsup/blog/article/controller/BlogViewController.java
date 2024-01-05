package me.kimyeonsup.blog.article.controller;

import java.util.List;
import lombok.RequiredArgsConstructor;
import me.kimyeonsup.blog.article.domain.dto.ArticleListViewResponse;
import me.kimyeonsup.blog.article.domain.dto.ArticleViewResponse;
import me.kimyeonsup.blog.article.domain.entity.Article;
import me.kimyeonsup.blog.article.service.ArticleService;
import me.kimyeonsup.blog.menu.domain.dto.CategoryResponse;
import me.kimyeonsup.blog.menu.service.CategoryService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@RequiredArgsConstructor
@Controller
public class BlogViewController {

    private final ArticleService articleService;
    private final CategoryService categoryService;

    @GetMapping("/articles")
    public String getArticles(Model model) {
        List<ArticleListViewResponse> articles = articleService.findAll().stream()
                .map(ArticleListViewResponse::new)
                .toList();
        List<CategoryResponse> categories = categoryService.findAll().stream()
                .map(CategoryResponse::new)
                .toList();
        model.addAttribute("articles", articles);
        model.addAttribute("categories", categories);

        return "articleList";
    }

    @GetMapping("/articles/{id}")
    public String getArticle(@PathVariable Long id, Model model) {
        Article article = articleService.findById(id);
        model.addAttribute("article", new ArticleViewResponse(article));

        return "article";
    }

    @GetMapping("/new-article")
    public String newArticle(@RequestParam(required = false) Long id, Model model) {
        if (id == null) {
            model.addAttribute("article", new ArticleViewResponse());
        } else {
            Article article = articleService.findById(id);
            model.addAttribute("article", new ArticleViewResponse(article));
        }

        return "newArticle";
    }
}
