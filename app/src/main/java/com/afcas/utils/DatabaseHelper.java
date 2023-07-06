package com.afcas.utils;

import java.sql.*;

public class DatabaseHelper {
    private static Connection connection;
    private static Statement statement = null;
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
            statement = connection.createStatement();
            System.out.println("Database connected successfully!");
        }
        return connection;
    }

    public static boolean isConnected() throws SQLException {
        return connection != null && !connection.isClosed();
    }

    public static ResultSet executeSQLStatement(String sqlStatement) {
        ResultSet resultSet = null;
        try {
            if (sqlStatement.trim().toLowerCase().startsWith("select")) {
                return statement.executeQuery(sqlStatement);
            } else {
                int rowsAffected = statement.executeUpdate(sqlStatement);
                System.out.println(rowsAffected + " row(s) affected.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return resultSet;
    }

    private static PreparedStatement createCommand(Connection conn, String spName, Object... parameterValues) throws SQLException {
        PreparedStatement res = conn.prepareStatement(spName);
        setParameters(res, parameterValues);
        return res;
    }

    private static Object executeCommand(String spName, SqlCommandExecutor executor, Object... parameterValues) throws SQLException {
        Object res = null;
        try (Connection conn = DatabaseHelper.getConnection();
             PreparedStatement cmd = createCommand(conn, spName, parameterValues)) {
            res = executor.execute(cmd);
        }
        return res;
    }

    private static void setParameters(PreparedStatement stmt, Object... parameterValues) throws SQLException {
        if (parameterValues != null) {
            for (int i = 0; i < parameterValues.length; i++) {
                stmt.setObject(i + 1, parameterValues[i]);
            }
        }
    }

    private interface SqlCommandExecutor {
        Object execute(PreparedStatement stmt) throws SQLException;
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