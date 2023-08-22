package com.example.chestGameServer.configs.MVC;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@FieldDefaults(makeFinal = true,level = AccessLevel.PRIVATE)
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {
    HttpAuthInterceptor httpAuthInterceptor;
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(httpAuthInterceptor);
    }
}