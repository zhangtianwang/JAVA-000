package spring.config;

import java.util.List;

public class School {

    /**
     * 学校名字
     */
    private String schoolName;

    /**
     * 面积
     */
    private Double area;

    private List<Student> students;

    public School(List<Student> students){
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

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }
}
