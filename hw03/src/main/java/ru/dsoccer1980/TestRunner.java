package ru.dsoccer1980;

import lombok.SneakyThrows;
import ru.dsoccer1980.annotations.AfterEach;
import ru.dsoccer1980.annotations.BeforeEach;
import ru.dsoccer1980.annotations.Test;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class TestRunner {

    public static void main(String[] args) {
        run(AnnotationClient.class);
    }

    private static void run(Class<?> testClass) {
        List<Method> listBeforeEach = new ArrayList<>();
        List<Method> listAfterEach = new ArrayList<>();
        Method[] declaredMethods = testClass.getDeclaredMethods();

        for (Method declaredMethod : declaredMethods) {
            AfterEach afterEach = declaredMethod.getAnnotation(AfterEach.class);
            if (afterEach != null) {
                listAfterEach.add(declaredMethod);
            }

            BeforeEach beforeEach = declaredMethod.getAnnotation(BeforeEach.class);
            if (beforeEach != null) {
                listBeforeEach.add(declaredMethod);
            }

        }

        for (Method declaredMethod : declaredMethods) {
            Test testAnnotation = declaredMethod.getAnnotation(Test.class);
            if (testAnnotation != null) {
                invokeMethodFromListByClass(listBeforeEach, testClass);
                invokeMethodByClass(declaredMethod, testClass);
                invokeMethodFromListByClass(listAfterEach, testClass);
            }
        }
    }

    @SneakyThrows
    private static void invokeMethodByClass(Method method, Class<?> clazz) {
        Object object = clazz.getDeclaredConstructor().newInstance();
        method.invoke(object);
    }

    private static void invokeMethodFromListByClass(List<Method> list, Class<?> clazz) {
        for (Method method : list) {
            invokeMethodByClass(method, clazz);
        }
    }
}
