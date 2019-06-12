package ru.dsoccer1980;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ReadFile {

    public static void main(String[] args) throws IOException {
        String fileName = "hw05/logs/gc.log";
        double sum = Files.lines(Paths.get(fileName)).filter(line -> line.contains("ms"))
                .map(line -> line.substring(line.lastIndexOf(" ") + 1))
                .mapToDouble(line -> Double.valueOf(line.replace("ms", "")))
                .sum();
        System.out.println(sum);
    }
}
