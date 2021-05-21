package com.yingda.lkj.config;

import com.yingda.lkj.utils.file.UploadUtil;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.ResourceBundle;

@Configuration
public class ResourcesConfig implements WebMvcConfigurer {
    private static final ResourceBundle bundle = ResourceBundle.getBundle("config");

    private final String resourcesPath = bundle.getString("resourcesPath");

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        UploadUtil.RESOURCE_LOCATIONS = resourcesPath;
        registry.addResourceHandler(UploadUtil.RESOURCES_URL + "/**")
                .addResourceLocations("file:" + resourcesPath).resourceChain(true);
    }
}
