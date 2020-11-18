package spring.definition;

import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class Starter {

    public static void main(String[] args) {
        BeanDefinitionBuilder beanBuilder = BeanDefinitionBuilder.genericBeanDefinition();
        beanBuilder.addPropertyValue("userName","zzl").addPropertyValue("age",30);
        AbstractBeanDefinition definition = beanBuilder.getBeanDefinition();
        definition.setBeanClass(Student.class);

        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.registerBeanDefinition("student",definition);
        context.refresh();
        Student student =  context.getBean(Student.class);
        System.out.println(student);
    }

}
