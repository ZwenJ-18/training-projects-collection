package com.supermarket.points.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("超市积分管理系统 API")
                        .version("1.0")
                        .description("覆盖积分查询、消费累计、积分兑换等核心功能")
                        .contact(new Contact()
                                .name("张文静")
                                .email("cyzjdm11226@outlook.com")));
    }
}
