package spring.factory;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class StudentFactory implements FactoryBean<Student> {
    @Override
    public Student getObject() throws Exception {
        return new Student();
    }

    @Override
    public Class<?> getObjectType() {
        return null;
    }

    @Override
    public boolean isSingleton() {
        return false;
    }
}
