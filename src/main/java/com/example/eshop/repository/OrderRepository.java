package com.example.eshop.repository;

import com.example.eshop.model.Order;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import static com.example.eshop.Constants.*;

public class OrderRepository {

    private static Connection _connection;
    List<Order> orderList;
    public OrderRepository() { }

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

    public List<Order> getAllOrdersList() {
        try {
            Order order;
            sqlConnection();
            orderList = new ArrayList<>();
            String sql = "SELECT * FROM orders";
            PreparedStatement statement = _connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                order = new Order();
                HashMap<Integer, Integer> map;
                order.setId(resultSet.getInt("id"));
                ObjectMapper objectMapper = new ObjectMapper();
                try {
                    map = objectMapper.readValue(resultSet.getString("products"), new TypeReference<HashMap<Integer, Integer>>(){});
                } catch (Exception e) {
                    map = new HashMap<>();
                }
                order.setProducts(map);
                order.setTotalPrice(resultSet.getBigDecimal("total_price"));
                order.setCustomerName(resultSet.getString("customer_name"));
                order.setCustomerAddress(resultSet.getString("customer_address"));
                order.setCustomerEmail(resultSet.getString("customer_email"));
                order.setPaymentStatus(resultSet.getBoolean("payment_status"));
                orderList.add(order);
            }
            return orderList;
        } catch (SQLException e) {
            // throw new RuntimeException(e);
        }
        return new ArrayList<>();
    }








}
