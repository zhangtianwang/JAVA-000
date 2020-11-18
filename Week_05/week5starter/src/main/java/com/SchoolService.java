package com;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


public class SchoolService {

    @Autowired
    private StudentService studentService;

    public void test(){
        studentService.say();
    }

}
