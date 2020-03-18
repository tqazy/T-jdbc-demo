package com.tqazy.jdbc;

import com.tqazy.entity.User;
import com.tqazy.utils.PropertiesUtils;
import com.tqazy.utils.ReflectionUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author 散场前的温柔
 */
public class JDBCUtils {

    private static Connection con;
    private static PreparedStatement ps;
    private static ResultSet rs;

    private static Map<String, String> map;

    /**
     * 获取数据库连接信息
     */
    private static void getProperties(String path) {
        List<String> list = new ArrayList<String>();
        list.add("driver");
        list.add("url");
        list.add("username");
        list.add("password");
        map = PropertiesUtils.readProperties(path, list);
    }

    /**
     * 获取数据库连接
     */
    public static void getConnection(String path) {
        try {
            if (map == null) {
                getProperties(path);
            }
            Class.forName(map.get("driver"));
            con = DriverManager.getConnection(map.get("url"), map.get("username"), map.get("password"));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static List<Object> select(String path, String sql, Class clazz, Object ... args){
        getConnection(path);
        // 如果创建连接失败，返回0行
        if(con == null){
            System.out.println("创建数据库连接失败");
            return null;
        }
        List<Object> list = new ArrayList<Object>();

        try{
            ps = con.prepareStatement(sql);
            for(int i=0;i<args.length;i++){
                ps.setObject(i+1, args[i]);
            }
            rs = ps.executeQuery();
            // 这里开始
            while(rs.next()){
                // 1. 利用反射创建对象
                Object obj = clazz.newInstance();

                // 2. 通过解析SQL语句来判断到底选择了哪些列，以及需要为obj对象的哪些属性赋值
                ResultSetMetaData rsmd = rs.getMetaData();
                for(int i=0;i<rsmd.getColumnCount();i++) {
                    String columnLabel = rsmd.getColumnLabel(i+1);
                    Object columnValue = rs.getObject(columnLabel);
                    ReflectionUtils.setFieldValueByParem(obj, columnLabel, columnValue);
                }
                list.add(obj);
            }
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            close();
        }
        return list;
    }


    /**
     * 关闭数据库相关连接，释放数据库资源
     */
    public static void close() {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
