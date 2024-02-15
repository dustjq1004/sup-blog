package me.kimyeonsup.blog.login.domain.dto;

import java.io.Serializable;
import lombok.Getter;
import me.kimyeonsup.blog.login.domain.entity.User;

@Getter
public class SessionUser implements Serializable {

    private String name;
    private String email;
    private String picture;

    public SessionUser(User user) {
        this.name = user.getUsername();
        this.email = user.getEmail();
        this.picture = user.getPicture();
    }
}
