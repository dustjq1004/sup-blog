package me.kimyeonsup.home.domain.blog.admin.article.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ArticleBatchDeleteRequest {

    private Long[] articleIds;

}
