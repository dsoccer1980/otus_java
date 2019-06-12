package ru.dsoccer1980;

import com.sun.management.GarbageCollectionNotificationInfo;

import javax.management.NotificationEmitter;
import javax.management.NotificationListener;
import javax.management.openmbean.CompositeData;
import java.lang.management.GarbageCollectorMXBean;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        List<String> list = new ArrayList<>();
        switchOnMonitoring();
        for (int i = 0; i < 100000; i++) {
            for (int j = 0; j < 600; j++) {
                list.add(new String(new char[j]));
            }
            for (int j = 0; j < 300; j++) {
                list.remove(j);
            }
        }
    }

    private static void switchOnMonitoring() {
        List<GarbageCollectorMXBean> gcbeans = java.lang.management.ManagementFactory.getGarbageCollectorMXBeans();

        for (GarbageCollectorMXBean gcbean : gcbeans) {
            System.out.println("GC name:" + gcbean.getName());
            NotificationEmitter emitter = (NotificationEmitter) gcbean;
            NotificationListener listener = (notification, handback) -> {
                if (notification.getType().equals(GarbageCollectionNotificationInfo.GARBAGE_COLLECTION_NOTIFICATION)) {
                    GarbageCollectionNotificationInfo info = GarbageCollectionNotificationInfo.from((CompositeData) notification.getUserData());
                    String gcName = info.getGcName();
                    String gcAction = info.getGcAction();
                    String gcCause = info.getGcCause();

                    long startTime = info.getGcInfo().getStartTime();
                    long duration = info.getGcInfo().getDuration();

                    System.out.println("start:" + startTime + " Name:" + gcName + ", action:" + gcAction + ", gcCause:" + gcCause + "(" + duration + " ms)");

                }
            };
            emitter.addNotificationListener(listener, null, null);
        }
    }
}

/*
Программа была запущена с параметрами
-Xms512m
-Xmx512m
-XX:+HeapDumpOnOutOfMemoryError
-Xlog:gc=debug:file=hw05/logs/gc-%p-%t.log:tags,uptime,time,level:filecount=5,filesize=10m
Программа падает с OutOfMemoryError


1) -XX:+UseG1GC
Программа работала: 5 min 19 sec
GC работал 18490.684 мс  - 5.8% от общей работы программы

Производительность: 202 цикла / 18,490684 с  = 10,92 цикл/сек


2) -XX:+UseSerialGC
Программа работала: 5 min 05 sec
GC работал 25829.646 мс  - 8.5% от общей работы программы

Производительность: 107 циклов / 25,829646 с = 4,41  цикл/сек

3)-XX:+UseParallelGC
Программа работала: 3 min 18 sec
GC работал 21899.405  мс  - 11.1% от общей работы программы

Производительность: 177 циклов / 21,899405 с = 8,08 цикл/сек


Вывод:
При данных параметрах эффективнее всего себя показал G1, производительность(цикл/сек) которого оказалась выше.


*/

