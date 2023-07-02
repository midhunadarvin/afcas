package com.afcas.utils;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StoredProcedureExecutor {
    private static CallableStatement createCommand(Connection conn, String spName, Object... parameterValues) throws SQLException {
        CallableStatement res = conn.prepareCall(spName);
        setParameters(res, parameterValues);
        return res;
    }

    private static Object executeCommand(String spName, SqlCommandExecutor executor, Object... parameterValues) throws Exception {
        Object res = null;
        Connection conn = DatabaseHelper.getConnection();
        if (conn == null || conn.isClosed()) {
            throw new Exception("Database connection is closed / not created!");
        }
        CallableStatement cmd = createCommand(conn, spName, parameterValues);
        res = executor.execute(cmd);

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
        Object execute(CallableStatement stmt) throws SQLException;
    }

    public static Object executeStoredProcedure(String spName, Object[] parameterValues) throws Exception {
        return executeCommand(spName, stmt -> {
            // Execute the stored procedure and process the result
            // Process the result set or perform other necessary actions
            return stmt.executeUpdate();
        }, parameterValues);
    }
}
