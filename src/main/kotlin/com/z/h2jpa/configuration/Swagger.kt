package com.z.h2jpa.configuration

import com.z.h2jpa.H2jpaApplication
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import springfox.documentation.builders.PathSelectors
import springfox.documentation.builders.RequestHandlerSelectors
import springfox.documentation.spi.DocumentationType
import springfox.documentation.spring.web.plugins.Docket
import springfox.documentation.swagger2.annotations.EnableSwagger2WebMvc


@Configuration
@EnableSwagger2WebMvc//enables swagger /swagger-ui.html
class Swagger {
    @Bean
    fun api(): Docket = Docket(DocumentationType.SWAGGER_2)
        .select()
        .apis(RequestHandlerSelectors.basePackage(H2jpaApplication::class.java.`package`.name))
        .paths(PathSelectors.any())
        .build()
}