package ru.netology.helpers;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class SqlHelper {
    private static SqlHelper instance;
    private Connection connection;

    private SqlHelper() {
        Properties env = new Properties();
        try {
            FileInputStream fis = new FileInputStream(".env");
            env.load(fis);
            fis.close();
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке файла .env: " + e.getMessage());
        }
        String url = System.getProperty("db.url");
        String username = env.getProperty("DB_USER");
        String password = env.getProperty("DB_PASS");

        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.err.println("Ошибка при подключении к базе данных: " + e.getMessage());
        }
    }

    public static synchronized SqlHelper getInstance() {
        if (instance == null) {
            instance = new SqlHelper();
        }
        return instance;
    }

    public String getLastPaymentStatus() {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM payment_entity ORDER BY created DESC LIMIT 1");
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String status = resultSet.getString("status");
                return status;
            } else {
                return "Таблица пуста";
            }
        } catch (SQLException e) {
            return "Ошибка при выполнении запроса: " + e.getMessage();
        }
    }

    public int getPaymentsCount() {
        try {
            PreparedStatement statement = connection.prepareStatement("SELECT COUNT(*) FROM payment_entity");
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return resultSet.getInt(1);
            } else {
                return 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // в случае ошибки возвращаем -1
        }
    }
}
