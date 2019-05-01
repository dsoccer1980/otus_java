package ru.dsoccer1980;

import lombok.SneakyThrows;
import ru.dsoccer1980.annotations.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MyJUnit {

    public static void run(Class<?> testClass) {
        List<Method> methodsBeforeEach = new ArrayList<>();
        List<Method> methodsAfterEach = new ArrayList<>();
        List<Method> methodsBeforeAll = new ArrayList<>();
        List<Method> methodsAfterAll = new ArrayList<>();
        List<Method> methodsTest = new ArrayList<>();
        Method[] declaredMethods = testClass.getDeclaredMethods();

        for (Method method : declaredMethods) {

            if (method.getAnnotation(AfterEach.class) != null) {
                methodsAfterEach.add(method);
            }

            if (method.getAnnotation(BeforeEach.class) != null) {
                methodsBeforeEach.add(method);
            }

            if (method.getAnnotation(BeforeAll.class) != null) {
                methodsBeforeAll.add(method);
            }

            if (method.getAnnotation(AfterAll.class) != null) {
                methodsAfterAll.add(method);
            }

            if (method.getAnnotation(Test.class) != null) {
                methodsTest.add(method);
            }
        }

        if (!methodsTest.isEmpty()) {

            if (!invokeMethodsFromListByObject(methodsBeforeAll, null)) {
                invokeMethodsFromListByObject(methodsAfterAll, null);
            } else {

                for (Method method : methodsTest) {
                    Object object = getInstanceOfClass(testClass);
                    if (!invokeMethodsFromListByObject(methodsBeforeEach, object)) {
                        invokeMethodsFromListByObject(methodsAfterEach, object);
                    } else {
                        invokeMethodByObject(method, object);
                        invokeMethodsFromListByObject(methodsAfterEach, object);
                    }

                }

                invokeMethodsFromListByObject(methodsAfterAll, null);
            }
        }
    }

    @SneakyThrows
    private static Object getInstanceOfClass(Class<?> testClass) {
        return testClass.getDeclaredConstructor().newInstance();
    }

    private static boolean invokeMethodByObject(Method method, Object object) {
        try {
            method.invoke(object);
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
        return true;
    }

    private static boolean invokeMethodsFromListByObject(List<Method> list, Object object) {
        boolean result = true;
        for (Method method : list) {
            if (!invokeMethodByObject(method, object)) {
                result = false;
            }
        }
        return result;
    }
}
