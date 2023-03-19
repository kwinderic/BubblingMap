package com.bubbling.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReflectUtil {
    /**
     * 将类的属性名和属性值放到map中
     * 2022-03-10 14:15:18 GMT+8
     * @param object
     * @return
     * @throws Exception
     * @author k
     */
    public static Map<String,Object> getObjectValue(Object object) throws Exception {
        Map<String, Object> map = new HashMap<>();
        if (object != null) {
            Class<?> clz = object.getClass();
            Field[] fields = clz.getDeclaredFields();
            for (Field field : fields) {
                // 类型是String
                if (field.getGenericType().toString().equals(
                        "class java.lang.String")) { // 如果type是类类型，则前面包含"class "，后面跟类名
                    Method m = (Method) object.getClass().getMethod(
                            "get" + getMethodName(field.getName()));
                    String val = (String) m.invoke(object);
                    if (val != null) {
                        map.put(field.getName(),val);
                    }
                }
                // 类型是Integer
                if (field.getGenericType().toString().equals(
                        "class java.lang.Integer")) {
                    Method m = (Method) object.getClass().getMethod(
                            "get" + getMethodName(field.getName()));
                    Integer val = (Integer) m.invoke(object);
                    if (val != null) {
                        map.put(field.getName(),val);
                    }
                }
                // 类型是Double
                if (field.getGenericType().toString().equals(
                        "class java.lang.Double")) {
                    Method m = (Method) object.getClass().getMethod(
                            "get" + getMethodName(field.getName()));
                    Double val = (Double) m.invoke(object);
                    if (val != null) {
                        map.put(field.getName(),val);
                    }
                }
                // 类型是Date
                if (field.getGenericType().toString().equals(
                        "class java.util.Date")) {
                    Method m = (Method) object.getClass().getMethod(
                            "get" + getMethodName(field.getName()));
                    Date val = (Date) m.invoke(object);
                    if (val != null) {
                        map.put(field.getName(),val);
                    }
                }
                // 类型是int
                if (field.getGenericType().toString().equals(
                        "int")) {
                    Method m = (Method) object.getClass().getMethod(
                            "get" + getMethodName(field.getName()));
                    int val = (int) m.invoke(object);
                    map.put(field.getName(),val);
                }
            }
        }
        return map;
    }

    /**
     * 把一个字符串的第一个字母大写、效率是最高的
     * 2022-03-10 14:14:42 GMT+8
     * @param fieldName
     * @return
     * @author k
     */
    private static String getMethodName(String fieldName){
        byte[] items = fieldName.getBytes();
        items[0] = (byte) ((char) items[0] - 'a' + 'A');
        return new String(items);
    }
}
