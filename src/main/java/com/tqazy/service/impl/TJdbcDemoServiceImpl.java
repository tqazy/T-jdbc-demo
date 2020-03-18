package com.tqazy.service.impl;

import com.tqazy.entity.Student;
import com.tqazy.entity.User;
import com.tqazy.jdbc.JDBCUtils;
import com.tqazy.service.TJdbcDemoService;

import java.util.List;

/**
 * @author 散场前的温柔
 */
public class TJdbcDemoServiceImpl implements TJdbcDemoService {

    public List<User> selectUserList(String name) {
        // 写SQL语句时，如果数据库字段名和对象属性名一致，则直接使用即可
        String sql = "SELECT name, password, age, remark FROM user WHERE name = ?";
        List<Object> list = JDBCUtils.select("database.properties", sql, User.class, name);
        List<User> userList = (List<User>)(List)list;
        return userList;
    }

    public List<Student> selectStudentList(String name) {
        // 写SQL语句时，如果数据库字段名和对象属性名不一致，那么需要在查询出的不一致的字段后面写上别名(此处别名就是对象属性名)，例如： student_name studentName
        String sql = "SELECT student_name studentName, student_sex studentSex, student_number studentNumber, school FROM student WHERE student_name = ?";
        List<Object> list = JDBCUtils.select("database.properties", sql, Student.class, name);
        List<Student> studentList = (List<Student>)(List)list;
        return studentList;
    }
}
