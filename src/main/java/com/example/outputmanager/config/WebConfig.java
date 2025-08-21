package com.example.outputmanager.config;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import lombok.RequiredArgsConstructor;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AuthInterceptor authInterceptor;

    /**
     * 画像アップロードの物理保存先ディレクトリ（環境ごとに上書き可能）
     * 例）dev: ./uploads/images, prod: /var/opt/outputmanager/uploads/images
     */
    @Value("${app.upload.dir:./uploads/images}")
    private String uploadDir;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/", "/home",
                        "/login/**", "/logout/**",
                        "/users/register/**",
                        "/css/**", "/js/**", "/images/**",
                        "/favicon.ico", "/error",
                        "/img/**" // アップロード画像 & プレースホルダの配信用
                );
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // /img/uploads/** -> file:{uploadDir}/ にマッピング
        registry.addResourceHandler("/img/uploads/**")
                .addResourceLocations(resolveUploadLocation(uploadDir))
                .setCachePeriod(3600); // 1時間のキャッシュ（必要に応じて調整）
    }

    private String resolveUploadLocation(String dir) {
        Path path = Paths.get(dir).toAbsolutePath().normalize();
        String p = path.toString();
        // 末尾スラッシュを保証
        if (!(p.endsWith("/") || p.endsWith("\\"))) {
            p = p + "/";
        }
        return "file:" + p;
    }
}