package me.kimyeonsup.home.domain.blog.article.domain.entity;

import lombok.extern.slf4j.Slf4j;
import me.kimyeonsup.home.domain.blog.admin.menu.domain.entity.Menu;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

@Slf4j
class ArticleTest {

    @ParameterizedTest
    @ValueSource(strings = {
            "![](https://elasticbeanstalk-ap-northeast-2-205011928457.s3.ap-northeast-2.amazonaws.com/images/elasticbeanstalk-ap-northeast-2-205011928457d9960479-74e3-4120-a926-00c7c4ed9aca-image.png)"
            ,
            "<img src=\"https://elasticbeanstalk-ap-northeast-2-205011928457.s3.ap-northeast-2.amazonaws.com/images/elasticbeanstalk-ap-northeast-2-2050119284579595c912-14a6-4e48-8d64-8a8e3aa65006-image.png\">"})
    void testSetArticleThumbnail(String content) {

        Article article = Article.builder()
                .title("title")
                .subTitle("subTitle")
                .author("dustjq1005")
                .menu(Menu.builder().id(1L).build())
                .content(content)
                .build();

        log.info("image Url : {}", article.getThumbnailUrl());
    }
}
