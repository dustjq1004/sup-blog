package me.kimyeonsup.blog.article.domain.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class UpdateArticleRequest {

    @NotBlank
    @Size(max = 50)
    private String title;

    @Size(max = 50)
    private String subTitle;
    private String content;

    @NotNull
    private Long menuId;
}
