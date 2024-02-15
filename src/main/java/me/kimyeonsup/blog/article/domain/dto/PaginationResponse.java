package me.kimyeonsup.blog.article.domain.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PaginationResponse<T> {

    private final int pageNumber;
    private final boolean isLast;
    private final long totalCount;
    private final List<T> data;
}
