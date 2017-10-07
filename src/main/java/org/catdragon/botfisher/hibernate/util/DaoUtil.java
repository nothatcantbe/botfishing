package org.catdragon.botfisher.hibernate.util;

import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;

import javax.persistence.Column;
import java.lang.reflect.Method;
import java.util.Set;

public class DaoUtil {
    public static String getTableName(Class clazz, String getter) {
        Reflections reflections = new Reflections("org.catdragon.botfisher.hibernate.pojo", new MethodAnnotationsScanner());

        Set<Method> ids = reflections.getMethodsAnnotatedWith(Column.class);
        for(Method method: ids) {
            if ((method.getDeclaringClass().equals(clazz)) && method.getName().equals(getter)) {
                Column annotation = method.getAnnotation(Column.class);
                String name = annotation.name();
                return name;
            }

        }
        return null;
    }
}
