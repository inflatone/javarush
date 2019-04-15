package com.javarush.task.task36.task3602;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Collections;

/* 
Найти класс по описанию
*/
public class Solution {
    public static void main(String[] args) {
        System.out.println(getExpectedClass());
    }

    public static Class getExpectedClass() {
        Class result = null;
        for (Class<?> clazz : Collections.class.getDeclaredClasses()) {
            int modifiers = clazz.getModifiers();
            if (!Modifier.isStatic(modifiers) || !Modifier.isPrivate(modifiers) || !implementsList(clazz)) {
                continue;
            }
            try {
                Method method = clazz.getMethod("get", int.class);
                if (method != null) {
                    Constructor<?> constructor = clazz.getDeclaredConstructor();
                    constructor.setAccessible(true);
                    Object o = constructor.newInstance();
                    method.setAccessible(true);
                    method.invoke(o, 0);
                }
            } catch (InvocationTargetException ite) {
                if (ite.getCause().getClass() == IndexOutOfBoundsException.class) {
                    result = clazz;
                    break;
                }
            } catch (Exception e) {
                //ignored
            }
        }
        return result;
    }

    private static boolean implementsList(Class<?> clazz) {
        boolean result = false;
        for (Class iface : clazz.getInterfaces()) {
            if (iface.getSimpleName().equals("List")) {
                result = true;
            }
        }
        return result || clazz.getSuperclass() != null && implementsList(clazz.getSuperclass());
    }
}
