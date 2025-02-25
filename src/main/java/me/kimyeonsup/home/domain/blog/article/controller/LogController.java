package me.kimyeonsup.home.domain.blog.article.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/log")
public class LogController {

    private static final Logger logger = LoggerFactory.getLogger(LogController.class);

    @GetMapping
    public String logTest() {
        logger.trace("TRACE 로그입니다.");
        logger.debug("DEBUG 로그입니다.");
        logger.info("INFO 로그입니다.");
        logger.warn("WARN 로그입니다.");
        logger.error("ERROR 로그입니다.");

        return "로그 테스트 완료!";
    }
}
