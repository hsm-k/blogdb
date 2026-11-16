package com.example.blogdb.config;

import com.example.blogdb.interceptor.LoginCheckInterceptor;
import com.example.blogdb.service.ContentImageStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    // @RequiredArgsConstructor가 이 final 필드를 받는 생성자를 자동으로 만듭니다.
    private final LoginCheckInterceptor loginCheckInterceptor;
    private final ContentImageStorageService contentImageStorageService;

    // 애플리케이션이 시작될 때 인터셉터의 적용 URL 규칙을 등록합니다.
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginCheckInterceptor)
                .addPathPatterns("/admin/**", "/content/**")
                .excludePathPatterns(
                        // 로그인 페이지까지 막으면 로그인 화면으로 무한 이동하므로 제외합니다.
                        "/admin/login",
                        // 로그아웃은 세션을 삭제하는 요청이므로 제외합니다.
                        "/admin/logout",
                        // 일반 방문자도 게시글 내 이미지를 볼 수 있도록 허용합니다.
                        "/content/image/**",
                        "/css/**",
                        "/*.ico",
                        "/error"
                );

    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/content/**")
                .addResourceLocations(contentImageStorageService.getResourceLocation());
    }
}
