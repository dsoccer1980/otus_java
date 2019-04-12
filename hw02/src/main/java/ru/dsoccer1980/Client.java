package ru.dsoccer1980;

import java.util.Collections;
import java.util.List;

public class Client {

    public static void main(String[] args) {
        List<Integer> list = new DIYArrayList<>();
        for (int i = 0; i < 50; i++) {
            Collections.addAll(list, i);
        }

        List<Integer> list2 = new DIYArrayList<>();
        for (int i = 100; i < 121; i++) {
            Collections.addAll(list2, i);
        }

        Collections.copy(list, list2);

        Collections.sort(list, Collections.reverseOrder());

        for (int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i) + " ");
        }

    }
}
