package ru.dsoccer1980.jdbc;

import ru.dsoccer1980.service.ExecutorHelper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.*;
import java.util.List;

public class JdbcTemplateImpl<T> implements JdbcTemplate<T> {

    private final Connection connection;
    private List<Field> listFields;
    private Field idField;
    private String insertSqlQuery;
    private String updateSqlQuery;
    private String selectSqlQuery;


    public JdbcTemplateImpl(Connection connection, Class<T> clazz) {
        this.connection = connection;
        listFields = ExecutorHelper.getAllFields(clazz);
        idField = ExecutorHelper.getIdField(clazz);
        insertSqlQuery = ExecutorHelper.getInsertSqlQuery(clazz, listFields);
        updateSqlQuery = ExecutorHelper.getUpdateSqlQuery(clazz, listFields);
        selectSqlQuery = ExecutorHelper.getSelectSqlQuery(clazz, listFields);
    }


    @Override
    public void create(T objectData) throws SQLException {
        Savepoint savePoint = this.connection.setSavepoint("savePointName");
        try (PreparedStatement pst = connection.prepareStatement(insertSqlQuery, Statement.RETURN_GENERATED_KEYS)) {
            ExecutorHelper.fillQueryParameters(pst, listFields, objectData);
            pst.executeUpdate();
        } catch (SQLException | IllegalAccessException ex) {
            this.connection.rollback(savePoint);
            ex.printStackTrace();
        }
    }

    @Override
    public void update(T objectData) throws SQLException {
        Savepoint savePoint = this.connection.setSavepoint("savePointName");
        try (PreparedStatement pst = connection.prepareStatement(updateSqlQuery, Statement.RETURN_GENERATED_KEYS)) {
            ExecutorHelper.fillQueryParameters(pst, listFields, objectData);
            pst.setObject(listFields.size() + 1, ExecutorHelper.getValueFromField(idField, objectData));
            pst.executeUpdate();
        } catch (SQLException | IllegalAccessException ex) {
            this.connection.rollback(savePoint);
            ex.printStackTrace();
        }
    }

    @Override
    public T load(long id, Class<T> clazz) {
        try (PreparedStatement preparedStatement = this.connection.prepareStatement(selectSqlQuery)) {
            preparedStatement.setLong(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                return ExecutorHelper.getInstance(resultSet, clazz);
            } catch (SQLException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }


}

