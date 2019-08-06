package ru.dsoccer1980;

public class Main {

    private int count = 0;
    private volatile boolean isThread1 = true;
    private boolean isReverse = false;


    public static void main(String[] args) {
        new Main().go();

    }

    private void inc(int nrThread) {
        for (int i = 0; i < 20; i++) {
            if (nrThread == 1) {
                while (!isThread1) {
                    Thread.onSpinWait();
                }
                System.out.println(Thread.currentThread().getName() + ":" + count);
                isThread1 = !isThread1;
            } else {
                while (isThread1) {
                    Thread.onSpinWait();
                }
                System.out.println(Thread.currentThread().getName() + ":" + count);
                if (isReverse) {
                    count--;
                } else {
                    count++;
                }
                if (count >= 10) {
                    isReverse = true;
                }
                isThread1 = !isThread1;
            }


        }


    }


    private void go() {

        Thread thread1 = new Thread(() -> inc(1));
        Thread thread2 = new Thread(() -> inc(2));

        thread1.start();
        thread2.start();


    }
}



