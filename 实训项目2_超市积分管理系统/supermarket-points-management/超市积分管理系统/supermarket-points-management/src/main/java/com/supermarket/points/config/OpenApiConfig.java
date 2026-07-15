package com.supermarket.points.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI supermarketPointsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("零售会员积分运营平台")
                        .description("面向超市/零售场景的会员积分管理、商品兑换、订单处理 RESTful API")
                        .version("v1.0.0")
                        .contact(new Contact().name("张文静").email("cyzjdm11226@outlook.com")));
    }
}
