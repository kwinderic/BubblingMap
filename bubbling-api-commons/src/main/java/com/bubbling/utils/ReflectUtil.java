package com.bubbling.utils;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ReflectUtil {
    /**
     * 将类的属性名和属性值放到map中
     * 2022-03-10 14:15:18 GMT+8
     * @throws Exception
     * @author k
     */
    public static Map<String,Object> getObjectValue(Object object) throws Exception {
        Map<String, Object> map = new HashMap<>();
        if (object != null) {
            Class<?> clz = object.getClass();
            Field[] fields = clz.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                Object value = field.get(object);
                map.put(field.getName(),value);
            }
        }
        return map;
    }
}
