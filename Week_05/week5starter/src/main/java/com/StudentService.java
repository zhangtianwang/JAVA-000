package com;

public class StudentService {

    public void say() {
        System.out.println("我的编号：" + this.id + "，我的姓名：" + this.name);
    }

    public StudentService(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    private int id;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
