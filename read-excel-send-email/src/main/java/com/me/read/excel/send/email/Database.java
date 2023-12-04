/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.me.read.excel.send.email;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    String username = "tsach";
    String password = "Hanoi123a@";
    public void Truncate() {
        System.out.println("Start truncate " + LocalDateTime.now());
        try {
            // Establish connection
            try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
                try (Statement statement = connection.createStatement()) {
                    statement.executeUpdate("TRUNCATE TABLE ZZZ_DPV_CBNV_CT");
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
            ArrayDescriptor arrayDescriptor = ArrayDescriptor.createDescriptor("ZZZ_DPV_CBNV_CT_TYPE_TABLE", connection);

            // Create an OracleConnection to unwrap the connection
            OracleConnection oracleConnection = connection.unwrap(OracleConnection.class);

            ARRAY arrayData = new ARRAY(arrayDescriptor, oracleConnection, data.toArray());

            // Prepare and execute the stored procedure call
            try (CallableStatement statement = connection.prepareCall("{call ZZ_INSERT_CBNV_CT(?)}")) {
                statement.setArray(1, arrayData);
                statement.execute();
                System.out.println("Data inserted successfully.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println("End ImportDB " + LocalDateTime.now());
    }
    
    public List<Object> Export(){
        System.out.println("Start ExportDB " + LocalDateTime.now());
        List<Object> data = new ArrayList<>();
        // Code
            // Establish connection
            try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
                try (CallableStatement callableStatement = connection.prepareCall("{call ZZZ_CBNV_CONG_APP(?)}")) {
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
                                item.add(resultSet.getString((String)itemHeader.get(i)));
                            }
                            data.add(item.toArray());
                        }
                    }
                }
            }
         catch (SQLException e) {
            e.printStackTrace();
        }
            
        System.out.println("End ExportDB " + LocalDateTime.now());
        return data;
    }
}
