package spring.annotation;

import org.springframework.stereotype.Component;

@Component("student")
public class Student {

    private String userName = "tianwang";

    private Integer age = 100;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }
}
