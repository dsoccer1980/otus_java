package ru.dsoccer1980;

import ru.dsoccer1980.annotations.AfterEach;
import ru.dsoccer1980.annotations.BeforeEach;
import ru.dsoccer1980.annotations.Test;

public class AnnotationClient {

    @BeforeEach
    public void beforeEach1() {
        System.out.println("beforeEach1");
    }

    @BeforeEach
    public void beforeEach2() {
        System.out.println("beforeEach2");
    }

    @Test
    public void test1() {
        System.out.println("Test1");
    }

    @Test
    public void test2() {
        System.out.println("Test2");
    }

    @Test
    public void test3() {
        System.out.println("Test3");
    }

    @AfterEach
    public void afterEach1() {
        System.out.println("afterEach1");
    }

    @AfterEach
    public void afterEach2() {
        System.out.println("afterEach2");
    }

    public void noAnnotation() {

    }
}
