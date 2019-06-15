package ru.dsoccer1980;

import com.google.gson.Gson;

import java.util.List;

public class Main {

    public static void main(String[] args) throws IllegalAccessException {
        MyObject myObject = new MyObject(1, "2", true, new int[]{41, 45, 46}, List.of("str1", "str2"));
        System.out.println(myObject);
        JsonImpl jsonImpl = new JsonImpl();
        String json = jsonImpl.toJson(myObject);

        Gson gson = new Gson();
        MyObject obj2 = gson.fromJson(json, MyObject.class);
        System.out.println(myObject.equals(obj2));
        System.out.println(obj2);
    }
}
