package com.tqazy.utils;


import org.springframework.util.Assert;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * @author 散场前的温柔
 */
public abstract class ReflectionUtils {
    private final static String SET = "set";
    private final static String GET = "get";
    private final static String IS = "is";

    /**
     * 拷贝两个对象
     *
     * @param source
     * @param target
     * @throws Exception
     */
    public static void copyProperties(Object source, Object target) throws Exception {
        //断言 目标对象不为空，否则抛出异常
        Assert.notNull(source, "Source must not be null");
        Assert.notNull(target, "Target must not be null");

        Map<String, Object> valueMap = getFieldValueMap(source);
        setFieldValue(target, valueMap);
    }

    /**
     * 把属性和属性值赋值到反射后的Object中
     *
     * @param obj 反射后的Object
     * @param name 属性名
     * @param value 属性值
     */
    public static void setFieldValueByParem(Object obj, String name ,Object value) throws Exception {
        Class<?> beanClass = obj.getClass();
        //获取bean的所有方法
        Method[] methods = beanClass.getDeclaredMethods();
        //获取bean的所有字段
        Field[] fields = beanClass.getDeclaredFields();
        for (Field field : fields) {
            try {
                String fieldName = field.getName();
                if(fieldName.equals(name)) {
                    //获取字段的类型
                    String fieldType = field.getType().getSimpleName();
                    if (("boolean".equals(fieldType) || "Boolean".equals(fieldType)) && "is".equals(fieldName.substring(0, 2))) {
                        fieldName = fieldName.substring(2);
                    }
                    //获取字段set的方法名
                    String fieldSetName = parGetOrSetName(fieldName, SET);
                    //获取字段的set方法
                    Method fieldSetMet = beanClass.getMethod(fieldSetName, field.getType());
                    //判断有没有该方法
                    if (!checkMethod(methods, fieldSetMet)) {
                        continue;
                    }
                    if (value != null) {
                        fieldSetMet.invoke(obj, value);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 取出bean的值放到map中
     *
     * @param bean
     * @return
     */
    private static Map<String, Object> getFieldValueMap(Object bean) throws Exception {
        Class<?> beanClass = bean.getClass();
        //获取bean的所有方法
        Method[] methods = beanClass.getDeclaredMethods();
        //获取bean的所有字段
        Field[] fields = beanClass.getDeclaredFields();

        Map<String, Object> valueMap = new HashMap<String, Object>(10);
        for (Field field : fields) {
            try {
                //获取字段的类型
                //String  boolean
                String fieldType = field.getType().getSimpleName();
                //java.lang.String  boolean
                String fieldType1 = field.getType().getName();

                Method fieldGetMet;
                String fieldName = field.getName();
                if ("boolean".equals(fieldType) || "Boolean".equals(fieldType)) {
                    if (!IS.equals(fieldName.substring(0, 2))) {
                        fieldName = IS + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
                    } else {
                        fieldName = IS + fieldName.substring(2, 3).toUpperCase() + fieldName.substring(3);
                    }
                    //获取boolean的isXxx方法
                    fieldGetMet = beanClass.getMethod(fieldName, new Class[]{});
                } else {
                    //获取字段get的方法名
                    String fieldGetName = parGetOrSetName(fieldName, GET);
                    //获取字段的get方法
                    fieldGetMet = beanClass.getMethod(fieldGetName, new Class[]{});
                }
                //判断有没有该方法
                if (!checkMethod(methods, fieldGetMet)) {
                    continue;
                }
                //获取字段的值
                Object fieldVal = fieldGetMet.invoke(bean, new Object[]{});
                if (fieldVal != null) {
                    valueMap.put(field.getName(), fieldVal);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return valueMap;
    }

    /**
     * 把属性值放到bean
     *
     * @param bean
     * @param valueMap
     */
    private static void setFieldValue(Object bean, Map<String, Object> valueMap) throws Exception {
        Class<?> beanClass = bean.getClass();
        //获取bean的所有方法
        Method[] methods = beanClass.getDeclaredMethods();
        //获取bean的所有字段
        Field[] fields = beanClass.getDeclaredFields();
        for (Field field : fields) {
            try {
                //获取字段的类型
                String fieldType = field.getType().getSimpleName();
                String fieldName = field.getName();
                if (("boolean".equals(fieldType) || "Boolean".equals(fieldType)) && "is".equals(fieldName.substring(0, 2))) {
                    fieldName = fieldName.substring(2);
                }
                //获取字段set的方法名
                String fieldSetName = parGetOrSetName(fieldName, SET);
                //获取字段的set方法
                Method fieldSetMet = beanClass.getMethod(fieldSetName, field.getType());
                //判断有没有该方法
                if (!checkMethod(methods, fieldSetMet)) {
                    continue;
                }
                Object value = valueMap.get(field.getName());
                if (value != null) {
                    fieldSetMet.invoke(bean, value);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 拼接set或get方法
     *
     * @param fieldName
     * @return
     */
    private static String parGetOrSetName(String fieldName, String met) {
        if (null == fieldName || "".equals(fieldName)) {
            return null;
        }
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
        if (met == null) {
            return false;
        }
        for (Method method : methods) {
            if (met.equals(method)) {
                return true;
            }
        }
        return false;
    }

}