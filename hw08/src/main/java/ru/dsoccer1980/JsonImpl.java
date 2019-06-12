package ru.dsoccer1980;

import javax.json.Json;
import javax.json.JsonArrayBuilder;
import javax.json.JsonObjectBuilder;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Collection;


public class JsonImpl {


    public String toJson(Object object) throws IllegalAccessException {
        JsonObjectBuilder objectBuilder = Json.createObjectBuilder();

        Field[] declaredFields = object.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            Class<?> typeObject = field.getType();
            Object objectValue = field.get(object);

            if (objectValue instanceof Number || objectValue instanceof Boolean ||
                    objectValue instanceof Character || objectValue instanceof String) {

                objectBuilder.add(field.getName(), objectValue.toString());

            } else if (typeObject.isArray()) {

                JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
                Object array = field.get(object);
                for (int index = 0; index < Array.getLength(array); index++) {
                    jsonArrayBuilder.add(Array.get(array, index).toString());
                }
                objectBuilder.add(field.getName(), jsonArrayBuilder);

            } else if (objectValue instanceof Collection) {

                JsonArrayBuilder jsonArrayBuilder = Json.createArrayBuilder();
                Collection list = (Collection) field.get(object);
                for (Object element : list) {
                    jsonArrayBuilder.add(element.toString());
                }
                objectBuilder.add(field.getName(), jsonArrayBuilder);
            }
        }

        return objectBuilder.build().toString();
    }
}
