package me.kimyeonsup.blog.menu.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.kimyeonsup.blog.article.repository.ArticleRepository;
import me.kimyeonsup.blog.login.domain.entity.User;
import me.kimyeonsup.blog.login.repository.UserRepository;
import me.kimyeonsup.blog.menu.domain.dto.AddMenuRequest;
import me.kimyeonsup.blog.menu.domain.dto.UpdateMenuRequest;
import me.kimyeonsup.blog.menu.domain.entity.Category;
import me.kimyeonsup.blog.menu.domain.entity.Menu;
import me.kimyeonsup.blog.menu.repository.CategoryRepository;
import me.kimyeonsup.blog.menu.repository.MenuRepository;
import me.kimyeonsup.blog.menu.service.MenuService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class MenuApiControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    MenuService menuService;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    MenuRepository menuRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    ArticleRepository articleRepository;

    User user;

    Category category;

    @BeforeEach
    void setSecurityContext() {
        userRepository.deleteAll();
        user = userRepository.save(User.builder()
                .email("dustjq1005@gmail.com")
                .password("test")
                .role("관리자")
                .build());

        Collection<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add((GrantedAuthority) () -> user.getRole());
        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(new UsernamePasswordAuthenticationToken(user,
                user.getPassword(), authorities));
    }

    @BeforeEach
    public void mockMvcSetUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .build();
        articleRepository.deleteAll();
        menuRepository.deleteAll();
        categoryRepository.deleteAll();
        category = categoryRepository.save(Category.builder()
                .name("언어")
                .menus(new ArrayList<>())
                .build());
    }

    @Test
    @DisplayName("AddMenu : 메뉴를 추가를 성공한다.")
    void addMenuTest() throws Exception {
        final String url = "/api/menu";
        final String name = "자바";
        final AddMenuRequest request = new AddMenuRequest(category.getId(), name);

        final String requestBody = objectMapper.writeValueAsString(request);

        Principal principal = Mockito.mock(Principal.class);
        Mockito.when(principal.getName()).thenReturn("username");

        // when
        ResultActions result = mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(requestBody));

        // then
        result.andExpect(status().isCreated());

        List<Menu> menus = menuRepository.findAll();

        assertThat(menus.size()).isEqualTo(1);
        assertThat(menus.get(0).getName()).isEqualTo(name);
    }

    @DisplayName("deleteMenu : 메뉴를 삭제한다.")
    @Test
    void deleteMenu() throws Exception {
        final String url = "/api/menu/{id}";
        Menu savededMenu = savedMenu(category);

        //when
        mockMvc.perform(delete(url, savededMenu.getId()))
                .andExpect(status().isOk());

        //then
        List<Menu> menus = menuRepository.findAll();

        assertThat(menus).isEmpty();
    }

    @DisplayName("메뉴 이름을 수정한다.")
    @Test
    void updateMenu() throws Exception {
        final String url = "/api/menu/{id}";
        Menu savedMenu = savedMenu(category);

        final String newName = "파이썬";

        UpdateMenuRequest request = new UpdateMenuRequest(category.getId(), newName);

        //when
        ResultActions result = mockMvc.perform(put(url, savedMenu.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(objectMapper.writeValueAsString(request)));

        //then
        result.andExpect(status().isOk());

        Menu category = menuRepository.findById(savedMenu.getId()).get();

        assertThat(category.getName()).isEqualTo(newName);
    }

    private Menu savedMenu(Category category) {
        return menuRepository.save(Menu.builder()
                .name("자바")
                .category(category)
                .build());
    }
}
