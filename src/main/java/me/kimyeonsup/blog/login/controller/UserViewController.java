package me.kimyeonsup.blog.login.controller;

import me.kimyeonsup.blog.config.oauth.OAuth2UserCustomService;
import me.kimyeonsup.blog.login.domain.dto.SessionUser;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Controller
public class UserViewController {

    @GetMapping("/login")
    public String login() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        SessionUser sessionUser = (SessionUser) attributes.getRequest()
                .getSession()
                .getAttribute(OAuth2UserCustomService.SESSION_USER_KEY);
        if (sessionUser != null) {
            return "redirect:/blog";
        }
        return "oauthLogin";
    }

    @GetMapping("/signup")
    public String signup() {
        return "signup";
    }
}
