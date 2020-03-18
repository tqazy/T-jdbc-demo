package com.tqazy.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author 散场前的温柔
 */
public abstract class ReflectionUtils {
    private final static String SET = "set";
    private final static String IS = "is";

    /**
     * 把属性和属性值赋值到反射后的Object中
     *
     * @param object    反射后的Object
     * @param columnLable   属性名
     * @param columnValue   属性值
     */
    public static void setFieldValueByParem(Object object, String columnLable, Object columnValue) throws Exception{
        // 将反射后的object取它的运行时类(即目标类)clazz
        Class clazz = object.getClass();
        // 把目标类的所有方法名取出形成数组methods
        Method[] methods = clazz.getDeclaredMethods();
        // 把目标类的所有属性名去除形成数组fields
        Field[] fields = clazz.getDeclaredFields();
        // 循环属性数组
        for (Field field : fields) {
            // 执行Field的getName()方法，取出属性名
            String fieldName = field.getName();
            // 指定Field的getType()方法，去取出类型的底层类简称
            String fieldType = field.getType().getSimpleName();
            // 如果类型是(boolean或Boolean)并且属性名以is开头的，属性名截去前两位。
            if (("boolean".equals(fieldType) || "Boolean".equals(fieldType)) && IS.equals(fieldName.substring(0, 2))) {
                fieldName = fieldName.substring(2);
            }
            // 如果解析出来的属性名和传入的属性名一直
            if (fieldName.equals(columnLable)) {
                // 生成属性名的set方法名
                String fieldSetName = parGetOrSetName(fieldName, SET);
                // 从目标类中get出需要的那个set方法
                Method fieldSetMet = clazz.getMethod(fieldSetName, field.getType());
                // 如果从方法名数组中找不到这个方法，那么就跳过
                if (!checkMethod(methods, fieldSetMet)) {
                    continue;
                }
                // 如果传入的属性值不为空
                if (columnValue != null) {
                    //通过找到的属性对应的set方法，把属性值赋值给object对象中，此时便完成了将属性值赋值给到反射后的object中
                    fieldSetMet.invoke(object, columnValue);
                }
            }
        }
    }

    /**
     * 生成属性名的set或get方法名
     *
     * @param fieldName 属性名
     * @param met "set" or "get"
     * @return
     */
    private static String parGetOrSetName(String fieldName, String met) {
        // 如果属性名为null或为空字符串，那么返回null
        if (null == fieldName || "".equals(fieldName)) {
            return null;
        }
        // 返回 方法名 = "set" + 属性名首字母大写 + 属性名除字母外的其他部分
        return met + fieldName.substring(0, 1).toUpperCase()
                + fieldName.substring(1);
    }

    /**
     * 判断类是否存在该方法
     *
     * @param methods
     * @param met
     * @return
     */
    private static boolean checkMethod(Method[] methods, Method met) {
        // 如果传入的方法为空，那么返回false
        if (met == null) {
            return false;
        }
        // 如果传入的方法再方法数组中找到，那么返回true
        for (Method method : methods) {
            if (met.equals(method)) {
                return true;
            }
        }
        return false;
    }
}