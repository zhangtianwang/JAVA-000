package com;

import com.SchoolService;
import com.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(StudentProperties.class)
public class AutoConfiguration {

    /**
     * 注入属性配置类
     */
    @Autowired
    private StudentProperties studentProperties;

    @Bean
    public StudentService studentService() {
        StudentService service = new StudentService(studentProperties.getId(),studentProperties.getName());
        return service;
    }

    @Bean
    public SchoolService schoolService() {
        SchoolService service = new SchoolService();
        return service;
    }

}
