package me.kimyeonsup.home.domain.blog.admin.article.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;


@SpringBootTest
@AutoConfigureMockMvc
class AdminArticleApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @DisplayName("언어 카테고리에 해당하는 id로 블로글 글을 조회를 한다.")
    @Test
    public void getArticlesByCategoryParamTest01() throws Exception {
        // given

        // when
        // then

    }
}