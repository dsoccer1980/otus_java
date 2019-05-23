package ru.dsoccer1980;

import lombok.SneakyThrows;
import ru.dsoccer1980.annotations.*;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class MyJUnit {
    private List<Method> methodsBeforeEach = new ArrayList<>();
    private List<Method> methodsAfterEach = new ArrayList<>();
    private List<Method> methodsBeforeAll = new ArrayList<>();
    private List<Method> methodsAfterAll = new ArrayList<>();
    private List<Method> methodsTest = new ArrayList<>();

    public static void run(Class<?> testClass) {
        MyJUnit myJUnit = new MyJUnit();

        getAnnotationMethods(testClass, myJUnit);
        invokeMethods(testClass, myJUnit);
    }

    private static void invokeMethods(Class<?> testClass, MyJUnit myJUnit) {
        if (!myJUnit.methodsTest.isEmpty()) {
            if (!invokeMethodsFromListByObject(myJUnit.methodsBeforeAll, null)) {
                invokeMethodsFromListByObject(myJUnit.methodsAfterAll, null);
            } else {
                for (Method method : myJUnit.methodsTest) {
                    Object object = getInstanceOfClass(testClass);
                    if (!invokeMethodsFromListByObject(myJUnit.methodsBeforeEach, object)) {
                        invokeMethodsFromListByObject(myJUnit.methodsAfterEach, object);
                    } else {
                        invokeMethodByObject(method, object);
                        invokeMethodsFromListByObject(myJUnit.methodsAfterEach, object);
                    }
                }
                invokeMethodsFromListByObject(myJUnit.methodsAfterAll, null);
            }
        }
    }

    private static void getAnnotationMethods(Class<?> testClass, MyJUnit myJUnit) {
        Method[] declaredMethods = testClass.getDeclaredMethods();

        for (Method method : declaredMethods) {
            if (method.getAnnotation(AfterEach.class) != null) {
                myJUnit.methodsAfterEach.add(method);
            } else if (method.getAnnotation(BeforeEach.class) != null) {
                myJUnit.methodsBeforeEach.add(method);
            } else if (method.getAnnotation(BeforeAll.class) != null) {
                myJUnit.methodsBeforeAll.add(method);
            } else if (method.getAnnotation(AfterAll.class) != null) {
                myJUnit.methodsAfterAll.add(method);
            } else if (method.getAnnotation(Test.class) != null) {
                myJUnit.methodsTest.add(method);
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
