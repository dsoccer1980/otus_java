package ru.dsoccer1980;

import java.util.concurrent.atomic.AtomicInteger;

public class Main {

    private int count = 0;
    private boolean isReverse = false;
    private AtomicInteger currentQueueThread = new AtomicInteger(1);

    public static void main(String[] args) {
        new Main().go();
    }

    private void go() {
        Thread thread1 = new Thread(() -> print(1));
        Thread thread2 = new Thread(() -> print(2));

        thread1.start();
        thread2.start();
    }

    private void print(int nrThread) {
        for (int i = 0; i < 20; i++) {
            if (nrThread == 1) {
                while (currentQueueThread.get() != 1) {
                    Thread.onSpinWait();
                }
                System.out.println("Поток1:" + count);
                currentQueueThread.incrementAndGet();
            } else {
                while (currentQueueThread.get() != 2) {
                    Thread.onSpinWait();
                }
                System.out.println("Поток2:" + count);
                if (isReverse) {
                    count--;
                } else {
                    count++;
                }
                if (count >= 10) {
                    isReverse = true;
                }
                currentQueueThread.decrementAndGet();
            }
        }
    }


}



