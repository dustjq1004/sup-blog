package me.kimyeonsup.home.domain.main.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

public class MainController {

    @GetMapping("/")
    public String mainPage(Model model) {


        return "";
    }
}
