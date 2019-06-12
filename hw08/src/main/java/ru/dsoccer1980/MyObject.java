package ru.dsoccer1980;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MyObject {
    private int value1;
    private String value2;
    private boolean value3;
    private int[] value4;
    private List<String> value5;

    public MyObject(int value1, String value2, boolean value3, int[] value4, List<String> value5) {
        this.value1 = value1;
        this.value2 = value2;
        this.value3 = value3;
        this.value4 = value4;
        this.value5 = value5;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MyObject myObject = (MyObject) o;
        return value1 == myObject.value1 &&
                value3 == myObject.value3 &&
                Objects.equals(value2, myObject.value2) &&
                Arrays.equals(value4, myObject.value4) &&
                Objects.equals(value5, myObject.value5);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(value1, value2, value3, value5);
        result = 31 * result + Arrays.hashCode(value4);
        return result;
    }

    @Override
    public String toString() {
        return "MyObject{" +
                "value1=" + value1 +
                ", value2='" + value2 + '\'' +
                ", value3=" + value3 +
                ", value4=" + Arrays.toString(value4) +
                ", value5=" + value5 +
                '}';
    }
}
