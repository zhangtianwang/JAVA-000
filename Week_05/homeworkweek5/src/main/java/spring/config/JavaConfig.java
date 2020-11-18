package spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Configuration
@ComponentScan
public class JavaConfig {

    @Bean("student")
    public Student createStudent(){
        return new Student();
    }

    @Bean("school")
    public School createSchool(){
        List<Student> students = new LinkedList<>();
        students.add(new Student());
        return new School(students);
    }
}
