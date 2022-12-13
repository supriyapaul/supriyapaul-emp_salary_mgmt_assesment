package com.proj.test.salarymanagement.config;

import com.proj.test.salarymanagement.service.impl.SearchRequestValidationServiceImpl;
import com.proj.test.salarymanagement.service.impl.UserValidationServiceImpl;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class ApplicationConfig {

    @Bean
    public DefaultListableBeanFactory defaultListableBeanFactory(){
        final DefaultListableBeanFactory beanFactory =
                new DefaultListableBeanFactory();
        return beanFactory;
    }

    // Can be configured from a property file to populate this Map.
    @Bean(name = "mapper")
    public Map<String,Class> mapper(){
        final Map<String,Class> mapper = new HashMap<>();
        mapper.put("user", UserValidationServiceImpl.class);
        mapper.put("search", SearchRequestValidationServiceImpl.class);
        return mapper;
    }
}
