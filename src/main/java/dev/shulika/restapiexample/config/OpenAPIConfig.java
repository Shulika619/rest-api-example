package dev.shulika.restapiexample.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI api() {
        return new OpenAPI()
                .servers(List.of(new Server().url("http://localhost:8080")))
                .info(new Info()
                        .title("Task Management System API")
                        .version("0.0.1")
                        .description("Simple REST API example")
                        .contact(new Contact().email("shulika619@gmail.com").name("Sergey Shulika").url("https://github.com/Shulika619"))

                );
    }

}
