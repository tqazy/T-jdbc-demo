package com.tqazy.entity;

/**
 * @author 散场前的温柔
 */
public class User {

    private Integer id;
    private String name;
    private String password;
    private Integer age;
    private String remark;

    public User() {}

    public User(String name, String password, Integer age, String remark) {
        this.name = name;
        this.password = password;
        this.age = age;
        this.remark = remark;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", age=" + age +
                ", remark='" + remark + '\'' +
                '}';
    }
}
