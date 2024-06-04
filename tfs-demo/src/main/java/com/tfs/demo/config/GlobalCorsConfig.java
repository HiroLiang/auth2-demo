package com.tfs.demo.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Slf4j
@Configuration
public class GlobalCorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        log.info("corsFilter");
        CorsConfiguration config = new CorsConfiguration();

        // 允許 cors 的來源
        config.addAllowedOrigin("/*");

        // 允許 cookie
        config.setAllowCredentials(false);

        // 允許的請求類型
        config.addAllowedMethod("/*");

        // 允許的 Header
        config.addAllowedHeader("/*");

        // 可以獲取的 Header
        config.addExposedHeader("/*");

        // 映射路徑
        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", config);
        return new CorsFilter(configSource);
    }
}
