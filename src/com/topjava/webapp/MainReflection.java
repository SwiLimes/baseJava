package com.topjava.webapp;

import com.topjava.webapp.model.Resume;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MainReflection {
    public static void main(String[] args) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        Resume r = new Resume();
        Field field = r.getClass().getDeclaredFields()[0];
        field.setAccessible(true);
        field.set(r, "reflectionUuid");

        Method methodToString = r.getClass().getMethod("toString");
        System.out.println(methodToString.invoke(r));
    }
}
