package com.afcas.utils;

import javax.sql.rowset.CachedRowSet;
import java.sql.SQLException;

public class CachedRowSetPrinter {

    public static void print(CachedRowSet cachedRowSet) throws SQLException {
        // Get the column count
        int columnCount = cachedRowSet.getMetaData().getColumnCount();

        // Calculate the total width for each column
        int totalWidth = columnCount * 15 + (columnCount + 1) * 3;

        // Print the table header
        for (int i = 1; i <= columnCount; i++) {
            String columnName = cachedRowSet.getMetaData().getColumnName(i);
            System.out.printf("| %-13s", columnName);
        }
        System.out.println("|");

        // Print the line separating header and data
        printLine(totalWidth);

        // Print the table rows
        while (cachedRowSet.next()) {
            for (int i = 1; i <= columnCount; i++) {
                String columnValue = cachedRowSet.getString(i);
                System.out.printf("| %-13s", columnValue);
            }
            System.out.println("|");
        }

        // Print the line separating data and footer
        printLine(totalWidth);
        cachedRowSet.beforeFirst();
    }

    // Method to print a line of '-' characters
    private static void printLine(int width) {
        for (int i = 0; i < width; i++) {
            System.out.print("-");
        }
        System.out.println();
    }
}
