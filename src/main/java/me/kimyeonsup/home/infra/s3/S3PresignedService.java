package me.kimyeonsup.home.infra.s3;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedPutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3PresignedService {

    private final S3Presigner s3Presigner;

    public Map<String, String> getPresignedUrl(String bucketName, String fileName) {
        final String regExp = "^(jpeg|png|gif|bmp)$";
        final String keyName = "images/" + bucketName + "%s-%s".formatted(UUID.randomUUID().toString(), fileName);

        try {

            String[] splitFileName = fileName.split("\\.");
            String extension = splitFileName[splitFileName.length - 1].toLowerCase();

            if (extension.equalsIgnoreCase("jpg")) {
                extension = "jpeg";
            }

            final String contentType = "image/" + extension;
            PutObjectRequest objectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(keyName)
                    .contentType(contentType)
                    .build();

            PutObjectPresignRequest presignRequest = PutObjectPresignRequest.builder()
                    .signatureDuration(Duration.ofMinutes(10))
                    .putObjectRequest(objectRequest)
                    .build();

            PresignedPutObjectRequest presignedRequest = s3Presigner.presignPutObject(presignRequest);
            final String signedUrl = presignedRequest.url().toString();

            return Map.of("signedUrl", signedUrl, "fileName", keyName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Map.of("signedUrl", null, "fileName", keyName);
    }
}
