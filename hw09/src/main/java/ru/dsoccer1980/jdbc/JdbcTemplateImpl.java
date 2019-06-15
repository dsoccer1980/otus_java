package ru.dsoccer1980.jdbc;

import ru.dsoccer1980.Annotaion.Id;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcTemplateImpl<T> implements JdbcTemplate<T> {

    private final Connection connection;

    public JdbcTemplateImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void create(T objectData) throws SQLException {
        if (!hasIdAnnotation(objectData.getClass())) {
            throw new IllegalArgumentException("There is no field with Id annotaion");
        }
        String sql = getInsertSqlQuery(objectData);
        Savepoint savePoint = this.connection.setSavepoint("savePointName");
        try (PreparedStatement pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            int index = 1;
            Field[] declaredFields = objectData.getClass().getDeclaredFields();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                pst.setObject(index++, field.get(objectData));
            }
            pst.executeUpdate();
        } catch (SQLException | IllegalAccessException ex) {
            this.connection.rollback(savePoint);
            ex.printStackTrace();
        }
    }

    @Override
    public void update(T objectData) throws SQLException {
        if (!hasIdAnnotation(objectData.getClass())) {
            throw new IllegalArgumentException("There is no field with Id annotaion");
        }
        String sql = getUpdateSqlQuery(objectData);
        Savepoint savePoint = this.connection.setSavepoint("savePointName");
        try (PreparedStatement pst = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            Field[] declaredFields = objectData.getClass().getDeclaredFields();
            int index = 1;
            for (Field field : declaredFields) {
                field.setAccessible(true);
                if (field.getAnnotation(Id.class) != null) {
                    pst.setObject(declaredFields.length, field.get(objectData));
                } else {
                    pst.setObject(index++, field.get(objectData));
                }
            }
            pst.executeUpdate();
        } catch (SQLException | IllegalAccessException ex) {
            this.connection.rollback(savePoint);
            ex.printStackTrace();
        }
    }

    @Override
    public T load(long id, Class<T> clazz) {
        if (!hasIdAnnotation(clazz)) {
            throw new IllegalArgumentException("There is no field with Id annotaion");
        }
        String sql = getSelectSqlQuery(clazz);

        try (PreparedStatement preparedStatement = this.connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return getInstance(resultSet, clazz);
            } catch (SQLException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String getInsertSqlQuery(T objectData) {
        Field[] declaredFields = objectData.getClass().getDeclaredFields();
        List<String> list = new ArrayList<>();
        StringBuilder columnNames = new StringBuilder();
        StringBuilder listQuestions = new StringBuilder();

        for (Field field : declaredFields) {
            field.setAccessible(true);
            list.add(field.getName());
            columnNames.append(field.getName()).append(",");
            listQuestions.append("?,");
        }

        columnNames.deleteCharAt(columnNames.length() - 1);
        listQuestions.deleteCharAt(listQuestions.length() - 1);
        String tableName = objectData.getClass().getSimpleName();

        return String.format("insert into %s(%s) values (%s)", tableName, columnNames, listQuestions);
    }

    private String getUpdateSqlQuery(T objectData) {
        Field[] declaredFields = objectData.getClass().getDeclaredFields();
        List<String> list = new ArrayList<>();
        String columnIdName = "";
        StringBuilder columnNames = new StringBuilder();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            if (field.getAnnotation(Id.class) != null) {
                columnIdName = field.getName();
            } else {
                list.add(field.getName());
                columnNames.append(field.getName()).append("=?,");
            }
        }

        columnNames.deleteCharAt(columnNames.length() - 1);

        String tableName = objectData.getClass().getSimpleName();
        return String.format("UPDATE %s SET %s WHERE %s=?", tableName, columnNames, columnIdName);
    }

    private String getSelectSqlQuery(Class<T> clazz) {
        Field[] declaredFields = clazz.getDeclaredFields();
        List<String> listFieldName = new ArrayList<>();
        String idColumnName = "";

        for (Field field : declaredFields) {
            if (field.getAnnotation(Id.class) != null) {
                idColumnName = field.getName();
            }
            field.setAccessible(true);
            listFieldName.add(field.getName());
        }
        if (idColumnName.equals("")) {
            throw new IllegalArgumentException("There is no field with Id annotaion");
        }

        String columnNames = String.join(",", listFieldName);
        String tableName = clazz.getSimpleName();

        return String.format("SELECT %s FROM %s WHERE %s=?", columnNames, tableName, idColumnName);
    }

    private T getInstance(ResultSet resultSet, Class<T> clazz) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException, SQLException {
        if (resultSet.next()) {
            T t = clazz.getDeclaredConstructor().newInstance();
            Field[] declaredFields = clazz.getDeclaredFields();
            for (Field field : declaredFields) {
                field.setAccessible(true);
                field.set(t, resultSet.getObject(field.getName()));
            }
            return t;
        }
        return null;
    }

    private boolean hasIdAnnotation(Class clazz) {
        Field[] declaredFields = clazz.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.getAnnotation(Id.class) != null) {
               return true;
            }
        }
        return false;
    }


}

