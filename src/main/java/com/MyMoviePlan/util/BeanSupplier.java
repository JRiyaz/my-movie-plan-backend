package com.MyMoviePlan.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class BeanSupplier {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

//    @Bean
//    public FilterRegistrationBean corsFilter() {
//        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        CorsConfiguration config = new CorsConfiguration();
//        config.setAllowCredentials(Boolean.TRUE);
//        config.addAllowedOrigin(CorsConfiguration.ALL);
//        config.addAllowedHeader(CorsConfiguration.ALL);
//        config.addAllowedMethod(CorsConfiguration.ALL);
//        source.registerCorsConfiguration("/**", config);
//        FilterRegistrationBean bean = new FilterRegistrationBean();
//        bean.setFilter(new CorsFilter());
//        bean.setOrder(0);
//        return bean;
//    }
}
