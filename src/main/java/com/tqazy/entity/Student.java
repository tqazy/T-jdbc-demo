package com.tqazy.entity;

/**
 * @author 散场前的温柔
 */
public class Student {

    private Integer id;
    private String studentName;
    private String studentSex;
    private Integer studentNumber;
    private String school;

    public Student() {}

    public Student(String studentName, String studentSex, Integer studentNumber, String school) {
        this.studentName = studentName;
        this.studentSex = studentSex;
        this.studentNumber = studentNumber;
        this.school = school;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getStudentSex() {
        return studentSex;
    }

    public void setStudentSex(String studentSex) {
        this.studentSex = studentSex;
    }

    public Integer getStudentNumber() {
        return studentNumber;
    }

    public void setStudentNumber(Integer studentNumber) {
        this.studentNumber = studentNumber;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", studentName='" + studentName + '\'' +
                ", studentSex='" + studentSex + '\'' +
                ", studentNumber=" + studentNumber +
                ", school='" + school + '\'' +
                '}';
    }
}
