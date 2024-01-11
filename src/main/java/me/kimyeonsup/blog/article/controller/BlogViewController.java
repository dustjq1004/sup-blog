package me.kimyeonsup.blog.article.controller;

import java.util.List;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import me.kimyeonsup.blog.article.domain.dto.ArticleListViewResponse;
import me.kimyeonsup.blog.article.domain.dto.ArticleResponse;
import me.kimyeonsup.blog.article.domain.dto.ArticleUpdatedResponse;
import me.kimyeonsup.blog.article.domain.entity.Article;
import me.kimyeonsup.blog.article.service.ArticleService;
import me.kimyeonsup.blog.menu.domain.dto.CategoryResponse;
import me.kimyeonsup.blog.menu.domain.dto.MenuResponse;
import me.kimyeonsup.blog.menu.service.CategoryService;
import me.kimyeonsup.blog.menu.service.MenuService;
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
    private final MenuService menuService;

    @GetMapping("/blog")
    public String getArticles(@RequestParam(required = false) Long menuId, Model model) {
        List<CategoryResponse> categories = categoryService.findAll().stream()
                .map(CategoryResponse::new)
                .toList();

        model.addAttribute("categories", categories);

        return "articleList";
    }

    @GetMapping("/articles")
    public String findAllArticles(@RequestParam(required = false) Long menuId, Model model) {
        List<ArticleListViewResponse> articles = null;
        if (Objects.isNull(menuId)) {
            articles = articleService.findAll()
                    .stream()
                    .map(ArticleListViewResponse::new)
                    .toList();
        } else {
            articles = articleService.findByMenuId(menuId)
                    .stream()
                    .map(ArticleListViewResponse::new)
                    .toList();
        }

        model.addAttribute("articles", articles);

        return "articleList :: #article-list";
    }

    @GetMapping("/articles/{id}")
    public String getArticle(@PathVariable Long id, Model model) {
        Article article = articleService.findById(id);
        model.addAttribute("article", new ArticleResponse(article));

        return "article";
    }

    @GetMapping("/new-article")
    public String newArticle(@RequestParam(required = false) Long id, Model model) {
        if (id == null) {
            model.addAttribute("article", new ArticleUpdatedResponse());
        } else {
            Article article = articleService.findById(id);
            model.addAttribute("article", new ArticleUpdatedResponse(article));
        }

        model.addAttribute("menus",
                menuService.findAll().stream()
                        .map(MenuResponse::new)
                        .toList());

        return "newArticle";
    }
}
