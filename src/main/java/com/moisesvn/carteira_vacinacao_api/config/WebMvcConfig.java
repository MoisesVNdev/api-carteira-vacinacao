package com.moisesvn.carteira_vacinacao_api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    /**
     * Configura os handlers de recursos estáticos para excluir /actuator/**
     * evitando conflito com os endpoints do Spring Actuator
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry
            .addResourceHandler("/static/**", "/public/**", "/resources/**")
            .addResourceLocations(
                "classpath:/static/",
                "classpath:/public/",
                "classpath:/resources/"
            )
            .setCachePeriod(31536000); // 1 ano em segundos
        
        // Garante que /actuator/** não seja tratado como recurso estático
        registry.addResourceHandler("/webjars/**")
            .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}
