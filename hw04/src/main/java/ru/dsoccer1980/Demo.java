package ru.dsoccer1980;

public class Demo {

    public static void main(String[] args) {
        ITestLogging testLogging = IoC.createTestLoggingClass();
        testLogging.calculation(6);
    }
}
