package me.kimyeonsup.home.domain.blog.admin.pagination;

import lombok.Builder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;

public class PageRequestBuilder {
    private int pageNumber = 0;
    private int pageSize = 10;
    private String sortBy = "createdAt";
    private Direction direction = Direction.DESC;

    @Builder
    public PageRequestBuilder(int pageNumber, int pageSize, String sortBy, String direction) {
        this.pageNumber = pageNumber;
        this.pageSize = pageSize;
        this.sortBy = sortBy;

        if (direction.equalsIgnoreCase("ASC")) {
            this.direction = Direction.ASC;
        }
    }

    public PageRequest createPageRequest() {
        return PageRequest.of(pageNumber, pageSize, direction, sortBy);
    }

    // 디폴드 값 정의
    // 생성자 빌더패턴
    // PageRequst 생성 메서드
    // 속성 - page, size, sort, 정렬 컬럼
}
