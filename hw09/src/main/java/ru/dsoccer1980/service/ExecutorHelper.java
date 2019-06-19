package ru.dsoccer1980.service;

import ru.dsoccer1980.Annotaion.Id;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExecutorHelper {

    public static List<Field> getAllFields(Class<?> clazz) {
        Field[] declaredFields = clazz.getDeclaredFields();
        return Arrays.asList(declaredFields);
    }

    public static Field getIdField(Class<?> clazz) {
        List<Field> fields = getAllFields(clazz);
        return fields.stream()
                .filter(f -> f.getAnnotation(Id.class) != null)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("There is no field with Id annotaion"));
    }

    public static <T> Object getValueFromField(Field field, T objectData) {
        field.setAccessible(true);
        try {
            return field.get(objectData);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> void fillQueryParameters(PreparedStatement preparedStatement, List<Field> fields, T objectData) throws SQLException, IllegalAccessException {
        for (int i = 0; i < fields.size(); i++) {
            fields.get(i).setAccessible(true);
            preparedStatement.setObject(i + 1, fields.get(i).get(objectData));
        }
    }

    public static <T> String getInsertSqlQuery(Class<T> clazz, List<Field> fields) {
        StringBuilder columnNames = new StringBuilder();
        StringBuilder listQuestions = new StringBuilder();

        for (Field field : fields) {
            field.setAccessible(true);
            columnNames.append(field.getName()).append(",");
            listQuestions.append("?,");
        }

        columnNames.deleteCharAt(columnNames.length() - 1);
        listQuestions.deleteCharAt(listQuestions.length() - 1);
        String tableName = getTableName(clazz);

        return String.format("insert into %s(%s) values (%s)", tableName, columnNames, listQuestions);
    }

    public static <T> String getUpdateSqlQuery(Class<T> clazz, List<Field> fields) {
        String columnIdName = "";
        StringBuilder columnNames = new StringBuilder();

        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getAnnotation(Id.class) != null) {
                columnIdName = field.getName();
            }
            columnNames.append(field.getName()).append("=?,");
        }
        columnNames.deleteCharAt(columnNames.length() - 1);

        String tableName = getTableName(clazz);
        return String.format("UPDATE %s SET %s WHERE %s=?", tableName, columnNames, columnIdName);
    }

    private static String getTableName(Class<?> clazz) {
        return clazz.getSimpleName();
    }

    public static <T> String getSelectSqlQuery(Class<T> clazz, List<Field> fields) {
        List<String> listFieldName = new ArrayList<>();
        String idColumnName = "";

        for (Field field : fields) {
            if (field.getAnnotation(Id.class) != null) {
                idColumnName = field.getName();
            }
            field.setAccessible(true);
            listFieldName.add(field.getName());
        }

        String columnNames = String.join(",", listFieldName);
        String tableName = clazz.getSimpleName();

        return String.format("SELECT %s FROM %s WHERE %s=?", columnNames, tableName, idColumnName);
    }

    public static <T> T getInstance(ResultSet resultSet, Class<T> clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, SQLException {
        if (resultSet.next()) {
            T instance = clazz.getDeclaredConstructor().newInstance();
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                field.set(instance, resultSet.getObject(field.getName()));
            }
            return instance;
        }
        return null;
    }
}
