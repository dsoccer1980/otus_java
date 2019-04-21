package ru.dsoccer1980;

import ru.dsoccer1980.annotations.*;

public class AnnotationClient {

    public AnnotationClient() {
        System.out.println();
        System.out.println("constructor");
    }

    @BeforeAll
    static void beforeAll() {
        System.out.println("BeforeAll");
    }

    @AfterAll
    static void afterAll() {
        System.out.println();
        System.out.println("AfterAll");
    }

    @BeforeEach
    public void beforeEach1() {
        System.out.println("beforeEach1");
        throw new RuntimeException("Runtime Exception in beforeEach1");
    }

    @BeforeEach
    public void beforeEach2() {
        System.out.println("beforeEach2");
    }

    @Test
    public void test1() {
        System.out.println("Test1");
        throw new RuntimeException("Runtime Exception in test1");
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
        System.out.println("no annotation");
    }
}
