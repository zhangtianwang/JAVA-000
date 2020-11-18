package jdbc;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

public class HikaricpUtils {

    private static HikariDataSource dataSource;

    static {
        InputStream is = null;
        try {
            is = HikaricpUtils.class.getClassLoader().getResourceAsStream("hikaricp.properties");
            // 加载属性文件并解析：
            Properties props = new Properties();
            props.load(is);
            HikariConfig config = new HikariConfig(props);
            dataSource = new HikariDataSource(config);
            Integer aa = 111;
        } catch (Exception e) {
            String msg = e.getMessage();
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (Exception ex) {

            }
        }

    }


    public static Connection getConnection()  {
        try {
            return dataSource.getConnection();
        } catch (Exception ex) {
            String msg = ex.getMessage();
        }
        return null;
    }

}
