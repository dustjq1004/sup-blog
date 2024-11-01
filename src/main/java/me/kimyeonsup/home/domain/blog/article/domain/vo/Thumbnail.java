package me.kimyeonsup.home.domain.blog.article.domain.vo;

import jakarta.persistence.Embeddable;
import lombok.Getter;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Getter
@Embeddable
public class Thumbnail {

    private static final Pattern IMG_TAG_PATTERN = Pattern.compile("(?i)<img[^>]*src=[\"']?([^>\"']+)[\"']?[^>]*>");
    private static final Pattern MD_IMG_TAG_PATTERN = Pattern.compile("^!\\[[a-zA-Z0-9]*\\]\\(([\\w:.\\/-]*)");
    private static final List<String> noImages = List.of("noimage01.png", "noimage02.png");
    private static final String NO_IMAGE_PREFIX = "/img/";

    private String url;

    public Thumbnail() {
    }

    public Thumbnail(String content) {
        this.url = extractUrl(content);
    }

    public static Thumbnail of(String content) {
        return new Thumbnail(content);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Thumbnail thumbnail = (Thumbnail) o;
        return Objects.equals(url, thumbnail.url);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(url);
    }

    private String extractUrl(String content) {
        Matcher match = IMG_TAG_PATTERN.matcher(content);
        if (match.find()) {
            return match.group(1);
        }

        match = MD_IMG_TAG_PATTERN.matcher(content);
        if (match.find()) {
            return match.group(1);
        }
        return "";
    }

    public String getUrl() {
        if (url.isEmpty()) {
            return NO_IMAGE_PREFIX.concat(noImages.get((int) (Math.random() * noImages.size())));
        }
        return url;
    }
}
