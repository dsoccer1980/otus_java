package ru.dsoccer1980;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;

class IoC {

    static ITestLogging createTestLoggingClass() {
        TestLogging testLogging = new TestLogging();
        InvocationHandler handler = new DemoInvocationHandler(testLogging);
        return (ITestLogging) Proxy.newProxyInstance(IoC.class.getClassLoader(),
                new Class<?>[]{ITestLogging.class}, handler);
    }

    static class DemoInvocationHandler implements InvocationHandler {
        private final ITestLogging testLogging;
        private List<String> logMethods = new ArrayList<>();

        DemoInvocationHandler(ITestLogging testLogging) {
            this.testLogging = testLogging;
            findLogMethods();
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (logMethods.contains(method.getName())) {
                System.out.println(String.format("executed method: %s, param: %s ", method.getName(), args[0]));
            }

            return method.invoke(testLogging, args);
        }

        private void findLogMethods() {
            Method[] declaredMethods = testLogging.getClass().getDeclaredMethods();
            for (Method declaredMethod : declaredMethods) {
                if (declaredMethod.getAnnotation(Log.class) != null) {
                    logMethods.add(declaredMethod.getName());
                }
            }
        }
    }
}
