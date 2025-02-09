package me.kimyeonsup.home.config;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("local")
@Configuration
public class EnvConfig {

    private final Dotenv dotenv;

    public EnvConfig() {
        this.dotenv = Dotenv.load();
    }

    @PostConstruct
    public void loadEnv() {
        dotenv.entries().forEach(entry ->
                System.setProperty(entry.getKey(), entry.getValue())
        );
    }

    @Bean
    public Map<String, String> envVariables() {
        return dotenv.entries()
                .stream()
                .collect(Collectors.toMap(entry -> entry.getKey(), entry -> entry.getValue()));
    }

    public String getEnv(String key) {
        return dotenv.get(key);
    }

}
