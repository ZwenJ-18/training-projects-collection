package com.scdb.studentcoursesystem.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * springdoc-openapi (Swagger UI) 配置
 * 访问地址: http://localhost:8080/swagger-ui.html
 */
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("学生选课管理系统 API")
                        .version("1.0")
                        .description("支持学生选课/退课、成绩管理、权限控制等功能")
                        .contact(new Contact()
                                .name("张文静")
                                .email("cyzjdm11226@outlook.com")));
    }
}
