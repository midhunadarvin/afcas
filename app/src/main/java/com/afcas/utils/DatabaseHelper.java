package com.afcas.utils;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetProvider;
import java.sql.*;

public class DatabaseHelper {
    private static Connection connection;
    private static String url, username, password, port = "";

    public static void init(String url, String port, String username, String password) {
        try {
            DatabaseHelper.url = "jdbc:postgresql://" + url + ( port != null && !port.isEmpty() ? ':' + port + "/" : "/");
            DatabaseHelper.username = username;
            DatabaseHelper.password = password;
            Class.forName("org.postgresql.Driver");
            getConnection();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(url, username, password);
            System.out.println("Database connected successfully!");
        }
        return connection;
    }

    public static boolean isConnected() throws SQLException {
        return connection != null && !connection.isClosed();
    }

    public static void closeStatement(Statement statement) {
        if (statement != null) {
            try {
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeResultSet(ResultSet resultSet) {
        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static CachedRowSet executeQuery(String sql) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            CachedRowSet cachedRowSet = RowSetProvider.newFactory().createCachedRowSet();
            preparedStatement = connection.prepareStatement(sql);
            resultSet = preparedStatement.executeQuery();
            cachedRowSet.populate(resultSet);
            return cachedRowSet;
        } catch (SQLException e) {
            // Handle exception
            e.printStackTrace();
            throw e;
        } finally {
            closeResultSet(resultSet);
            closeStatement(preparedStatement);
        }
    }

    public static CachedRowSet executeQuery(String sql, Object[] parameterValues) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            CachedRowSet cachedRowSet = RowSetProvider.newFactory().createCachedRowSet();
            preparedStatement = connection.prepareStatement(sql);
            setParameters(preparedStatement, parameterValues);
            resultSet = preparedStatement.executeQuery();
            cachedRowSet.populate(resultSet);
            return cachedRowSet;
        } catch (SQLException e) {
            // Handle exception
            e.printStackTrace();
            throw e;
        } finally {
            closeResultSet(resultSet);
            closeStatement(preparedStatement);
        }
    }

    // Example method to execute an INSERT, UPDATE, or DELETE query
    public static int executeUpdate(String sql, Object[] parameterValues) throws SQLException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try {
            connection = getConnection();
            preparedStatement = connection.prepareStatement(sql);
            setParameters(preparedStatement, parameterValues);
            return preparedStatement.executeUpdate();
        } catch (SQLException e) {
            // Handle exception
            e.printStackTrace();
            throw e;
        } finally {
            closeStatement(preparedStatement);
        }
    }

    private static void setParameters(PreparedStatement stmt, Object... parameterValues) throws SQLException {
        if (parameterValues != null) {
            for (int i = 0; i < parameterValues.length; i++) {
                stmt.setObject(i + 1, parameterValues[i]);
            }
        }
    }

    public static Object executeStoredProcedure(String spName, Object[] parameterValues) throws Exception {
        return StoredProcedureExecutor.executeStoredProcedure(spName, parameterValues);
    }

    public static Object executeScalar(String spName, Object[] parameterValues) throws SQLException {
        Object result = null;
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            conn = getConnection();
            stmt = conn.prepareStatement(spName);
            setParameters(stmt, parameterValues);

            boolean hasResultSet = stmt.execute();
            if (hasResultSet) {
                ResultSet rs = stmt.getResultSet();
                if (rs.next()) {
                    result = rs.getObject(1);
                }
            }
        } finally {
            stmt.close();
        }

        return result;
    }
}