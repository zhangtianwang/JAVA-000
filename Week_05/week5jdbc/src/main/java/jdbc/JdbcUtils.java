package jdbc;


import javafx.application.Application;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class JdbcUtils {

    private static String url;
    private static String user;
    private static String password;

    static {
        Properties properties = new Properties();
        try {
            ClassLoader classLoader = JdbcUtils.class.getClassLoader();
            InputStream inputStream = classLoader.getResourceAsStream("jdbc.yml");
            properties.load(inputStream);
            url = properties.getProperty("url");
            user = properties.getProperty("user");
            password = properties.getProperty("password");
            Class.forName(properties.getProperty("driver"));
        } catch (Exception e) {
            e.printStackTrace();
            String msg = e.getMessage();
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, password);
    }

    // 5.定义关闭资源的方法
    public static void close(Connection conn, Statement stmt, ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
            }
        }
        if (stmt != null) {
            try {
                stmt.close();
            } catch (SQLException e) {
            }
        }
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
            }
        }
    }

    public static void close(Connection conn, Statement stmt) {
        close(conn, stmt, null);
    }

}
