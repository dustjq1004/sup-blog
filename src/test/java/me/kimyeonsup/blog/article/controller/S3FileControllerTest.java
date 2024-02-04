package me.kimyeonsup.blog.article.controller;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import me.kimyeonsup.blog.infra.s3.S3PresignedService;
import me.kimyeonsup.blog.login.domain.entity.User;
import me.kimyeonsup.blog.login.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;


@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
class S3FileControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private S3PresignedService s3PresignedService;

    @Autowired
    private UserRepository userRepository;

    User user;

    @Value("${cloud.s3.bucket}")
    private String bucketName;

    @BeforeEach
    void setSecurityContext() {
        userRepository.deleteAll();
        user = userRepository.save(User.builder()
                .email("dustjq1005@gmail.com")
                .password("test")
                .build());

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user,
                user.getPassword(), user.getAuthorities()));
    }

    @BeforeEach
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
    }

    @DisplayName("getPreSigendUrl : S3에서 preSignedUrl을 가져온다.")
    @Test
    void getPreSigendUrlTest() {
        // given
        String fileName = "aaa.png";

        // when
        Map<String, String> responseData = s3PresignedService.getPresignedUrl(bucketName, fileName);
//        log.info("responseData : {}", responseData);
        String url = responseData.get("signedUrl");

        // then
        assertThat(url).startsWith(
                "https://elasticbeanstalk-ap-northeast-2-205011928457.s3.ap-northeast-2.amazonaws.com/");
    }
}