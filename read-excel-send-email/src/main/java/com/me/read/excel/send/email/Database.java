/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.me.read.excel.send.email;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import oracle.jdbc.OracleConnection;
import oracle.sql.ARRAY;
import oracle.sql.ArrayDescriptor;

/**
 *
 * @author dienpv
 */
public class Database {

    // Set up Oracle database connection
    String jdbcUrl = "jdbc:oracle:thin:@//10.2.254.69:1521/betest";
    //String username = "zcc";
    //String password = "1234567890aA@";
    String username = System.getenv("DB_USER");
    String password = System.getenv("DB_PASSWORD");
    public boolean isConsistent = false;
    public void BackUpAndTruncate() {
        System.out.println("Start truncate " + LocalDateTime.now());
        try {
            // Establish connection
            try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
                try (Statement statement = connection.createStatement()) {
                    statement.addBatch("insert into ZCC.ZZZ_DPV_CBNV_CT_HIS select * from zcc.ZZZ_DPV_CBNV_CT");
                    statement.addBatch("TRUNCATE TABLE ZCC.ZZZ_DPV_CBNV_CT");
                    statement.executeBatch();
                    System.out.println("Table truncated successfully.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("End truncate " + LocalDateTime.now());
    }

    public void Import(List<Object> data) {
        System.out.println("Start ImportDB " + LocalDateTime.now());
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            // Create an ArrayDescriptor for EmployeeTableType
            ArrayDescriptor arrayDescriptor = ArrayDescriptor.createDescriptor("ZCC.ZZZ_DPV_CBNV_CT_TYPE_TABLE", connection);

            // Create an OracleConnection to unwrap the connection
            OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);

            ARRAY arrayData = new ARRAY(arrayDescriptor, oracleConnection, data.toArray());

            // Prepare and execute the stored procedure call
            try (CallableStatement statement = connection.prepareCall("{call ZCC.ZZ_INSERT_CBNV_CT(?)}")) {
                statement.setArray(1, arrayData);
                statement.execute();
                System.out.println("Data inserted successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("End ImportDB " + LocalDateTime.now());
    }

    public List<Object> Export() {
        System.out.println("Start ExportDB " + LocalDateTime.now());
        List<Object> data = new ArrayList<>();
        // Code
        // Establish connection
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
            try (CallableStatement callableStatement = connection.prepareCall("{call ZCC.ZZZ_CBNV_CONG_APP" + (isConsistent ? "_DPV" : "") + "(?)}")) {
                // Register the OUT parameter for the result set
                callableStatement.registerOutParameter(1, oracle.jdbc.OracleTypes.CURSOR);

                // Execute the stored procedure
                callableStatement.execute();

                // Retrieve the result set from the OUT cursor
                try (ResultSet resultSet = (ResultSet) callableStatement.getObject(1)) {
                    List<Object> itemHeader = new ArrayList<>();
                    for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                        itemHeader.add(resultSet.getMetaData().getColumnName(i));
                    }
                    data.add(itemHeader.toArray());
                    while (resultSet.next()) {
                        List<Object> item = new ArrayList<>();
                        for (int i = 0; i < itemHeader.size(); i++) {
                            item.add(resultSet.getString((String) itemHeader.get(i)));
                        }
                        data.add(item.toArray());
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("End ExportDB " + LocalDateTime.now());
        return data;
    }
}
