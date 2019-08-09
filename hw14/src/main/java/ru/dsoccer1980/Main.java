package ru.dsoccer1980;

import java.util.concurrent.ConcurrentLinkedQueue;

public class Main {

    private ConcurrentLinkedQueue<Thread> queue = new ConcurrentLinkedQueue<>();

    public static void main(String[] args) {
        new Main().go();
    }

    private void go() {
        Thread thread1 = new Thread(this::print);
        Thread thread2 = new Thread(this::print);

        queue.add(thread1);
        queue.add(thread2);

        thread1.start();
        thread2.start();
    }

    private void print() {
        for (int i = 0; i <= 10; i++) {
            operate(i);
        }

        for (int i = 9; i >= 0; i--) {
            operate(i);
        }
    }

    private void operate(int i) {
        while (!Thread.currentThread().equals(queue.peek())) {
            Thread.onSpinWait();
        }
        System.out.println(Thread.currentThread().getName() + ":" + i);
        queue.add(queue.poll());
    }


}



