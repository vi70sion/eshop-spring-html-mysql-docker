package com.example.eshop.repository;

import com.example.eshop.model.Order;
import com.example.eshop.model.OrderProduct;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.sql.*;
import java.util.*;

import static com.example.eshop.Constants.*;

public class OrderRepository {

    ProductRepository prRepository = new ProductRepository();
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

    public List<Order> findAllOrders() {
        try {
            Order order;
            sqlConnection();
            orderList = new ArrayList<>();
            String sql = "SELECT * FROM orders";
            PreparedStatement statement = _connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                order = new Order();
                order.setId(resultSet.getInt("id"));
                String jsonInput = resultSet.getString("products");
                List<OrderProduct> orderProductList = fromJsonToOrderProductList(jsonInput);
                order.setProducts(orderProductList);
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

    public List<Order> findOrdersByPaymentStatus(String paymentStatus) {
        try {
            Order order;
            sqlConnection();
            orderList = new ArrayList<>();
            String sql = "SELECT * FROM orders WHERE payment_status = ?";
            PreparedStatement statement = _connection.prepareStatement(sql);
            boolean status = paymentStatus.equalsIgnoreCase("paid");
            statement.setBoolean(1, status);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                order = new Order();
                order.setId(resultSet.getInt("id"));
                String jsonInput = resultSet.getString("products");
                List<OrderProduct> orderProductList = fromJsonToOrderProductList(jsonInput);
                order.setProducts(orderProductList);
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


    public BigDecimal orderAmount(long id) {
        try {
            sqlConnection();
            String sql = "SELECT * FROM orders WHERE id = ?";
            PreparedStatement statement = _connection.prepareStatement(sql);
            statement.setLong(1, id);
            ResultSet resultSet = statement.executeQuery();
            boolean hasResults = resultSet.next();
            if(!hasResults) return BigDecimal.ZERO;
            String result = resultSet.getString("products");
            List<OrderProduct> orderProductList = fromJsonToOrderProductList(result);
            if(orderProductList.isEmpty()) return BigDecimal.ZERO;
            BigDecimal amount = BigDecimal.valueOf(0.00);
            for (OrderProduct item : orderProductList) {
                long productId = item.getProductId();
                int quantity = item.getQuantity();
                amount = amount.add(prRepository.getProductById(productId).getPrice().multiply(BigDecimal.valueOf(quantity)));
            }
            return amount;
        } catch (SQLException e) {
            return BigDecimal.ZERO;
        }
    }

    // add order to database; set payment status to false; recalculate order amount and update value in database
    public BigDecimal addOrder(Order order, UUID uuid) {
        try {
            sqlConnection();
            String sql = "INSERT INTO orders ( products, total_price, customer_name, customer_address, customer_email, payment_status, uuid) VALUES (?,?,?,?,?,?,?)";
            PreparedStatement statement = _connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS);
            statement.setString(1, listToJsonString(order.getProducts()));
            statement.setBigDecimal(2, order.getTotalPrice());
            statement.setString(3, order.getCustomerName());
            statement.setString(4, order.getCustomerAddress());
            statement.setString(5, order.getCustomerEmail());
            statement.setBoolean(6, false);
            statement.setBytes(7, toBytes(uuid));
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                // inserted order ID
                ResultSet resultSet = statement.getGeneratedKeys();
                if (resultSet.next()) {
                    long id = resultSet.getInt(1);
                    BigDecimal amount = orderAmount(id);
                    sql = "UPDATE orders SET total_price = ? WHERE id = ?";
                    statement = _connection.prepareStatement(sql);
                    statement.setBigDecimal(1, amount);
                    statement.setLong(2, id);
                    if (statement.executeUpdate() > 0) return amount;
                }
            }
        } catch (SQLException e) {
            //throw new RuntimeException(e);
        }
        return BigDecimal.ZERO;
    }

    public boolean setOrderPaymentStatusTrue(UUID uuid){
        try {
            sqlConnection();
            String sql = "UPDATE orders SET payment_status = ? WHERE uuid = ?";
            PreparedStatement statement = _connection.prepareStatement(sql);
            statement.setBoolean(1, true);
            statement.setBytes(2, toBytes(uuid));
            if (statement.executeUpdate() > 0) return true;
        } catch (SQLException e) {
            //throw new RuntimeException(e);
        }
        return false;
    }


    public List<OrderProduct> fromJsonToOrderProductList(String jsonInput){
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<OrderProduct> orderProductList = objectMapper.readValue(jsonInput, new TypeReference<List<OrderProduct>>() {});
            return orderProductList;
        } catch (Exception e) {
            //klaida konvertuojant json objektus i lista
            return new ArrayList<>();
        }
    }

    public byte[] toBytes(UUID uuid) {
        ByteBuffer bb = ByteBuffer.wrap(new byte[16]);
        bb.putLong(uuid.getMostSignificantBits());
        bb.putLong(uuid.getLeastSignificantBits());
        return bb.array();
    }

    public UUID fromBytes(byte[] bytes) {
        ByteBuffer bb = ByteBuffer.wrap(bytes);
        long mostSigBits = bb.getLong();
        long leastSigBits = bb.getLong();
        return new UUID(mostSigBits, leastSigBits);
    }

    // Convert List<OrderProduct> to JSON string
    public String listToJsonString(List<OrderProduct> productList){
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String jsonString = objectMapper.writeValueAsString(productList);
            return jsonString;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return "";
    }

}
