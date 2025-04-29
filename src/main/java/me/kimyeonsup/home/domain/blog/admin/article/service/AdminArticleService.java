package me.kimyeonsup.home.domain.blog.admin.article.service;

import lombok.RequiredArgsConstructor;
import me.kimyeonsup.home.domain.blog.admin.article.domain.entity.AdminArticle;
import me.kimyeonsup.home.domain.blog.admin.article.repository.AdminArticleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminArticleService {

    private final AdminArticleRepository adminArticleRepository;

    /*
     * 필터 - 카테고리, 메뉴
     * 정렬 - 제목, 시간 순으로 정렬 가능
     * Paging 처리
     * */
    public Page<AdminArticle> findArticlesBy(int pageNumber, Long categoryId, Long menuId) {
        PageRequest pageRequest = PageRequest.of(pageNumber, 10, Sort.by(Direction.DESC, "createdAt"));
        // 1. 필터 구현 - 카테고리
        // 2. 필터 구현 - 메뉴
        // 3. 정렬 구현 - 제목, 시간 순으로 정렬 가능
        return adminArticleRepository.findAll(pageRequest);
    }

}
