package me.kimyeonsup.home.domain.blog.article.controller;

import java.util.Map;

import lombok.RequiredArgsConstructor;
import me.kimyeonsup.home.infra.s3.S3PresignedService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/s3/image")
public class S3FileController {

    @Value("${cloud.s3.bucket}")
    private String bucketName;
    private final S3PresignedService s3PresignedService;

    @GetMapping
    public ResponseEntity<Map<String, String>> getSignedUrl(@RequestParam String fileName) {
        Map<String, String> responseMap = s3PresignedService.getPresignedUrl(bucketName, fileName);
        return ResponseEntity.ok()
                .body(responseMap);
    }
}
