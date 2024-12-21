package com.zzk.config;



import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
@Slf4j
public class JdbcConfig {
    // MySQL JDBC URL, 用户名, 密码
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/data?useUnicode=true&allowPublicKeyRetrieval=true&characterEncoding=utf-8&useSSL=false&serverTimezone=GMT%2B8";
    private static final String JDBC_USER = "root";
    private static final String JDBC_PASSWORD = "123456";

    // 静态代码块用于加载MySQL JDBC驱动
    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            log.info("MySQL JDBC驱动已成功加载。");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("无法加载MySQL JDBC驱动", e);
        }
    }

    /**
     * 获取数据库连接
     *
     * @return 数据库连接对象
     * @throws SQLException 如果发生SQL异常
     */
    public static Connection getConnection() throws SQLException {
        Connection connection = DriverManager.getConnection(JDBC_URL, JDBC_USER, JDBC_PASSWORD);
        if (connection != null && !connection.isClosed()) {
            log.info("成功获取到数据库连接。");
        }
        return connection;
    }

}
