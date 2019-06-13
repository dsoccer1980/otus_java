package ru.dsoccer1980;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;


public class JsonImpl {


    public String toJson(Object object) throws IllegalAccessException {
        if (object == null) {
            return "null";
        }
        if (object instanceof Number || object instanceof Boolean) {
            return object.toString();
        }
        if (object instanceof Character || object instanceof String) {
            return "\"" + object.toString() + "\"";
        }

        if (object.getClass().isArray()) {
            return getJsonFromArray(object);
        }

        if (object instanceof Collection) {
            return getJsonFromCollection((Collection) object);
        }

        StringBuilder stringBuilder = new StringBuilder("{");
        Field[] declaredFields = object.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            Object objectValue = field.get(object);
            stringBuilder.append("\"").append(field.getName()).append("\":");
            stringBuilder.append(toJson(objectValue)).append(",");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append("}");

        return stringBuilder.toString();
    }

    private String getJsonFromCollection(Collection object) throws IllegalAccessException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        for (Object element : object) {
            stringBuilder.append(toJson(element)).append(",");
        }

        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    private String getJsonFromArray(Object object) throws IllegalAccessException {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        for (int index = 0; index < Array.getLength(object); index++) {
            stringBuilder.append(toJson(Array.get(object, index))).append(",");
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append("]");
        return stringBuilder.toString();
    }
}
