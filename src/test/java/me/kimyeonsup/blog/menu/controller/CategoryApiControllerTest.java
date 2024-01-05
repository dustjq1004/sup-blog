package me.kimyeonsup.blog.menu.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import me.kimyeonsup.blog.login.domain.entity.User;
import me.kimyeonsup.blog.login.repository.UserRepository;
import me.kimyeonsup.blog.menu.domain.dto.AddCategoryRequest;
import me.kimyeonsup.blog.menu.domain.dto.UpdateCategoryRequest;
import me.kimyeonsup.blog.menu.domain.entity.Category;
import me.kimyeonsup.blog.menu.repository.CategoryRepository;
import me.kimyeonsup.blog.menu.repository.MenuRepository;
import me.kimyeonsup.blog.menu.service.CategoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest
@AutoConfigureMockMvc
class CategoryApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    CategoryService categoryService;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    UserRepository userRepository;

    User user;

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
        menuRepository.deleteAll();
        categoryRepository.deleteAll();
    }

    @Test
    @DisplayName("AddCategory : 카테고리 추가를 성공한다.")
    void addCategoryTest() throws Exception {
        final String url = "/api/category";
        final String name = "언어";
        final AddCategoryRequest userRequest = new AddCategoryRequest(name);

        final String requestBody = objectMapper.writeValueAsString(userRequest);

        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("username");

        // when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .principal(principal)
                .content(requestBody));

        // then
        result.andExpect(status().isCreated());

        List<Category> categories = categoryRepository.findAll();

        assertThat(categories.size()).isEqualTo(1);
        assertThat(categories.get(0).getName()).isEqualTo(name);
    }

    @DisplayName("deleteArticle : 카테고리를 삭제한다.")
    @Test
    void deleteCategory() throws Exception {
        final String url = "/api/category/{id}";
        Category savedCategory = savedCategory();

        //when
        mockMvc.perform(delete(url, savedCategory.getId()))
                .andExpect(status().isOk());

        //then
        List<Category> categories = categoryRepository.findAll();

        assertThat(categories).isEmpty();
    }

    @DisplayName("카테고리 이름을 수정한다.")
    @Test
    void updateCategory() throws Exception {
        final String url = "/api/category/{id}";
        Category savedCategory = savedCategory();

        final String newName = "인프라";

        UpdateCategoryRequest request = new UpdateCategoryRequest(newName);

        //when
        ResultActions result = mockMvc.perform(put(url, savedCategory.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)));

        //then
        result.andExpect(status().isOk());

        Category category = categoryRepository.findById(savedCategory.getId()).get();

        assertThat(category.getName()).isEqualTo(newName);
    }

    private Category savedCategory() {
        return categoryRepository.save(Category.builder()
                .name("언어")
                .menus(new ArrayList<>())
                .build());
    }

}