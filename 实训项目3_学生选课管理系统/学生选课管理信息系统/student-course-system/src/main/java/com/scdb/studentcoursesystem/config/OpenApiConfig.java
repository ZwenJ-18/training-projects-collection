package com.scdb.studentcoursesystem.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI studentCourseOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("高校教务选课服务平台")
                        .description("面向高校场景的选课、退课、成绩管理与统计报表 RESTful API")
                        .version("v1.0.0")
                        .contact(new Contact().name("张文静").email("cyzjdm11226@outlook.com")));
    }
}
