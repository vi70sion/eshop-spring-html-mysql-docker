package com.example.eshop.repository;

import com.example.eshop.model.User;
import java.sql.*;
import static com.example.eshop.Constants.*;

public class UserRepository {
    private static Connection _connection;
    public UserRepository() {
    }

    public static void sqlConnection() {
        try {
            _connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException e) {
            // connection issues
            System.err.println("SQL Exception: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            // handle any other exceptions
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public int userLogin(User user) {
        try {
            sqlConnection();
            String sql = "SELECT * FROM users WHERE name = ? AND password = ?";
            PreparedStatement statement = _connection.prepareStatement(sql);
            statement.setString(1, user.getName());
            statement.setString(2, user.getPassword());
            ResultSet resultSet = statement.executeQuery();
            return (resultSet.next()) ? resultSet.getInt("id"): -1;
        } catch (SQLException e) {
            return -1;
        }
    }

}
