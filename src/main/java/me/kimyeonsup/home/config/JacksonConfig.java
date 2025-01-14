package me.kimyeonsup.home.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.kimyeonsup.home.config.jackson.NullToEmptySerializerModifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // null 값을 빈 문자열로 변환
        objectMapper.setSerializerFactory(
                objectMapper.getSerializerFactory().withSerializerModifier(new NullToEmptySerializerModifier())
        );
        return objectMapper;
    }

}
