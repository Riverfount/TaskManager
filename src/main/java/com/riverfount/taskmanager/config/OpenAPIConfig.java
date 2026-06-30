package com.riverfount.taskmanager.config;

import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;

@Configuration
public class OpenAIConfig {
    @Bean
    public OpenAPI openAIConfig() {
        OpenAPI openAPI = new OpenAPI();
        openAPI.info(
                new Info()
                        .title("Task Manager")
                        .version("1.0")
                        .description("A Simple Task Manager"));
        return openAPI;
    }
}
