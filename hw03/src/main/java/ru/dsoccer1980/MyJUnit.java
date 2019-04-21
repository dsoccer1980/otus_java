package ru.dsoccer1980;

import lombok.SneakyThrows;
import ru.dsoccer1980.annotations.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MyJUnit {

    public static void run(Class<?> testClass) {
        List<Method> listBeforeEach = new ArrayList<>();
        List<Method> listAfterEach = new ArrayList<>();
        List<Method> listBeforeAll = new ArrayList<>();
        List<Method> listAfterAll = new ArrayList<>();
        boolean hasTestAnnotation = false;
        Method[] declaredMethods = testClass.getDeclaredMethods();

        for (Method declaredMethod : declaredMethods) {

            if (declaredMethod.getAnnotation(AfterEach.class) != null) {
                listAfterEach.add(declaredMethod);
            }

            if (declaredMethod.getAnnotation(BeforeEach.class) != null) {
                listBeforeEach.add(declaredMethod);
            }

            if (declaredMethod.getAnnotation(BeforeAll.class) != null) {
                listBeforeAll.add(declaredMethod);
            }

            if (declaredMethod.getAnnotation(AfterAll.class) != null) {
                listAfterAll.add(declaredMethod);
            }

            if (declaredMethod.getAnnotation(Test.class) != null) {
                hasTestAnnotation = true;
            }
        }

        if (hasTestAnnotation) {

            invokeMethodsFromListByObject(listBeforeAll, null);

            for (Method declaredMethod : declaredMethods) {
                Test testAnnotation = declaredMethod.getAnnotation(Test.class);
                if (testAnnotation != null) {
                    Object object = getInstanceOfClass(testClass);
                    invokeMethodsFromListByObject(listBeforeEach, object);
                    invokeMethodByObject(declaredMethod, object);
                    invokeMethodsFromListByObject(listAfterEach, object);
                }
            }
            invokeMethodsFromListByObject(listAfterAll, null);
        }
    }

    @SneakyThrows
    private static Object getInstanceOfClass(Class<?> testClass) {
        return testClass.getDeclaredConstructor().newInstance();
    }

    @SneakyThrows
    private static void invokeMethodByObject(Method method, Object object) {
        try {
            method.invoke(object);
        } catch (Exception e) {
            //nothing to do
        }
    }

    private static void invokeMethodsFromListByObject(List<Method> list, Object object) {
        for (Method method : list) {
            invokeMethodByObject(method, object);
        }
    }
}
