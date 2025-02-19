package com.kraken.spring_app_coworking.Security.Config;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
//@Configuration
public class CorsConfig {
    //    CORS
//    averiguar los metodos de este cors
//    @Bean
//    CorsConfigurationSource corsConfigurationSource(){
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowedOriginPatterns(Arrays.asList("*"));
//        config.setAllowedMethods(Arrays.asList("GET","POST","DELETE","PUT"));
//        config.setAllowedHeaders(Arrays.asList("Authorization","Content-Type"));
//        config.setAllowCredentials(true);
//
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**",config);
//        return source;
//    }
//
//    @Bean
//    FilterRegistrationBean<CorsFilter> corsFilter(){
//        FilterRegistrationBean<CorsFilter> corsBean= new FilterRegistrationBean<>
//                (new CorsFilter(corsConfigurationSource()));
//        corsBean.setOrder(Ordered.HIGHEST_PRECEDENCE);
//        return corsBean;
//    }
}
