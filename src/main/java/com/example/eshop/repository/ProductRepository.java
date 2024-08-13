package com.example.eshop.repository;

import com.example.eshop.model.Product;
import static com.example.eshop.Constants.*;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductRepository {

    List<Product> prodList;
    private static Connection _connection;

    public ProductRepository() {
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

    public List<Product> getAllProductsList() {
        try {
            Product product;
            sqlConnection();
            prodList = new ArrayList<>();
            String sql = "SELECT * FROM products";
            PreparedStatement statement = _connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                product = new Product();
                product.setId(resultSet.getInt("id"));
                product.setName(resultSet.getString("name"));
                product.setDescription(resultSet.getString("description"));
                product.setPrice(resultSet.getBigDecimal("price"));
                product.setCategory(resultSet.getString("category"));
                product.setImageUrl(resultSet.getString("imageUrl"));
                prodList.add(product);
            }
            return prodList;
        } catch (SQLException e) {
            // throw new RuntimeException(e);
        }
        return new ArrayList<>();
    }

    public boolean addProduct(Product product) {
        try {
            sqlConnection();
            String sql = "INSERT INTO products ( name, description, price, category, imageUrl) VALUES (?,?,?,?,?)";
            PreparedStatement statement = _connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, product.getName());
            statement.setString(2, product.getDescription());
            statement.setBigDecimal(3, product.getPrice());
            statement.setString(4, product.getCategory());
            statement.setString(5, product.getImageUrl());
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                // įterptos eilutės ID
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    int id = resultSet.getInt(1);
                    return true;
                }
            }
        } catch (SQLException e) {
            //throw new RuntimeException(e);
        }
        return false;
    }

}
