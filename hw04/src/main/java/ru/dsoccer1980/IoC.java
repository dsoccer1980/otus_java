package ru.dsoccer1980;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

class IoC {

    static ITestLogging createTestLoggingClass() {
        TestLogging testLogging = new TestLogging();
        InvocationHandler handler = new DemoInvocationHandler(testLogging);
        return (ITestLogging) Proxy.newProxyInstance(IoC.class.getClassLoader(),
                new Class<?>[]{ITestLogging.class}, handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final ITestLogging testLogging;

        DemoInvocationHandler(ITestLogging testLogging) {
            this.testLogging = testLogging;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            Method[] declaredMethods = testLogging.getClass().getDeclaredMethods();
            for (Method declaredMethod : declaredMethods) {
                if (declaredMethod.getName().equals(method.getName()) && declaredMethod.getAnnotation(Log.class) != null) {
                    System.out.println(String.format("executed method: %s, param: %s ", method.getName(), args[0]));
                }
            }
            return method.invoke(testLogging, args);
        }
    }
}
