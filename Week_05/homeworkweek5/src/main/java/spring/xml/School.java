package spring.xml;

import java.util.List;

public class School {

    private String schoolName;

    private Double area;

    private Student student;

    private List<Student> students;

    public School(Student student, List<Student> students){
        this.student = student;
        this.students = students;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName;
    }

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }
}
