package com.mariocabral.intercop.challenge.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.function.Predicate;

import static springfox.documentation.builders.PathSelectors.regex;

@EnableSwagger2
@Configuration
public class SwaggerConfiguration {
    /**
     * Publish a bean to generate swagger2 endpoints
     * @return a swagger configuration bean
     */
    @Bean
    public Docket usersApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(usersApiInfo())
                .select()
                .paths(userPaths())
                .apis(RequestHandlerSelectors.basePackage("com.mariocabral.intercop.challenge.controller"))
                .build()
                .useDefaultResponseMessages(false);
    }

    private ApiInfo usersApiInfo() {
        return new ApiInfoBuilder()
                .title("Service User")
                .version("1.0")
                .license("Apache License Version 2.0")
                .build();
    }

    private Predicate<String> userPaths() {
        return regex("/.*");
    }
}
