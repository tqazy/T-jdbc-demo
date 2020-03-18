package com.tqazy.test;

import com.tqazy.entity.Student;
import com.tqazy.entity.User;
import com.tqazy.service.TJdbcDemoService;
import com.tqazy.service.impl.TJdbcDemoServiceImpl;
import org.junit.Test;

import java.util.List;

/**
 * @author 散场前的温柔
 */
public class TestJdbc {

    @Test
    public void testJdbc(){
        TJdbcDemoService service = new TJdbcDemoServiceImpl();

        List<User> list = service.selectUserList("苏熙");
        if (list == null) {
            System.out.println("用户不存在");
        } else {
            for (User user : list) {
                System.out.println(user.toString());
            }
        }

        List<Student> list2 = service.selectStudentList("苏熙");
        if (list2 == null) {
            System.out.println("学生不存在");
        } else {
            for (Student student : list2) {
                System.out.println(student.toString());
            }
        }
    }
}
