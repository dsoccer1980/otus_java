package ru.dsoccer1980;

public class TestLogging implements ITestLogging {

    @Log
    public void calculation(int param) {
        System.out.println("Inside of calculation method");
    }
}
