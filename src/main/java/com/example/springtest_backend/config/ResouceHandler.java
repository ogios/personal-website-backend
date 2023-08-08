package com.example.springtest_backend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.resource.ContentVersionStrategy;
import org.springframework.web.servlet.resource.VersionResourceResolver;

@Configuration
public class ResouceHandler implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        VersionResourceResolver versionResourceResolver = new VersionResourceResolver()
                .addVersionStrategy(new ContentVersionStrategy(), "/**");
        registry
                .addResourceHandler("/raw/text/*")
                .addResourceLocations("file:public/raw/text/")
//                .setCachePeriod(2592000)
                .setCacheControl(CacheControl.noCache())
                .resourceChain(true)
                .addResolver(versionResourceResolver);
        WebMvcConfigurer.super.addResourceHandlers(registry);
    }
}
