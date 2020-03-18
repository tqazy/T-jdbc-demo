package com.tqazy.service;

import com.tqazy.entity.Student;
import com.tqazy.entity.User;

import java.util.List;

/**
 * @author 散场前的温柔
 */
public interface TJdbcDemoService {

    /**
     * 查询用户列表
     * @param name
     * @return
     */
    List<User> selectUserList(String name);

    /**
     * 查询学生列表
     * @param name
     * @return
     */
    List<Student> selectStudentList(String name);

}
