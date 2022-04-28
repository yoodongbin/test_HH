package com.readToExcel.ReserveData.dbConnect;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JdbcConnect {
        public static Driver jdbcConnect() {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                System.out.println("hh : Driver Loading Success");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }
        public static Connection getConnection() { // 커넥션 해주는 역할, DB객체

            jdbcConnect();
            Connection conn = null;

            try {

                conn = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/HHSamples?useSSL=false&allowPublicKeyRetrieval=true", "root", String.valueOf(220106));
                System.out.println("Connection Success!!");

            } catch (SQLException e) {
                System.out.println("hh : DB를 연결하지 못했습니다");
                e.printStackTrace();
            }
            return conn;
        }
}
