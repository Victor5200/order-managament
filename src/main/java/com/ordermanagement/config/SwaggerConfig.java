package com.ordermanagement.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl("http://localhost:8080");
        devServer.setDescription("Server URL in local environment");

        Contact contact = new Contact();
        contact.setEmail("victoralexsander5200@gmail.com");
        contact.setName("Victor Cordeiro");
        contact.setUrl("https://github.com/victor5200");

        Info info = new Info()
                .title("Order Management API")
                .version("1.0")
                .contact(contact)
                .description("Api de gestão de pedidos.");

        return new OpenAPI().info(info).servers(List.of(devServer));
    }
}
