package com.example.eshop.repository;

import com.example.eshop.model.Order;
import com.example.eshop.model.OrderProduct;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
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

//    public List<Order> getAllOrdersList() {
//        try {
//            Order order;
//            sqlConnection();
//            orderList = new ArrayList<>();
//            String sql = "SELECT * FROM orders";
//            PreparedStatement statement = _connection.prepareStatement(sql);
//            ResultSet resultSet = statement.executeQuery();
//            while (resultSet.next()) {
//                order = new Order();
//                HashMap<Integer, Integer> map;
//                order.setId(resultSet.getInt("id"));
//                ObjectMapper objectMapper = new ObjectMapper();
//                try {
//                    map = objectMapper.readValue(resultSet.getString("products"), new TypeReference<HashMap<Integer, Integer>>(){});
//                } catch (Exception e) {
//                    map = new HashMap<>();
//                }
//                order.setProducts(map);
//                order.setTotalPrice(resultSet.getBigDecimal("total_price"));
//                order.setCustomerName(resultSet.getString("customer_name"));
//                order.setCustomerAddress(resultSet.getString("customer_address"));
//                order.setCustomerEmail(resultSet.getString("customer_email"));
//                order.setPaymentStatus(resultSet.getBoolean("payment_status"));
//                orderList.add(order);
//            }
//            return orderList;
//        } catch (SQLException e) {
//            // throw new RuntimeException(e);
//        }
//        return new ArrayList<>();
//    }

    public List<Order> getAllOrdersList() {
        try {
            Order order;
            List<OrderProduct> orderProductList;
            sqlConnection();
            orderList = new ArrayList<>();
            String sql = "SELECT * FROM orders";
            PreparedStatement statement = _connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                try {
                    order = new Order();
                    orderProductList = new ArrayList<>();
                    order.setId(resultSet.getInt("id"));
                    String jsonInput = resultSet.getString("products");
                    ObjectMapper objectMapper = new ObjectMapper();
                    JsonNode rootNode = objectMapper.readTree(jsonInput);
                    for (JsonNode node : rootNode) {
                        Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
                        while (fields.hasNext()) {
                            Map.Entry<String, JsonNode> entry = fields.next();
                            int productId = Integer.parseInt(entry.getKey());  // Get productId
                            int quantity = entry.getValue().asInt();  // Get quantity
                            OrderProduct orderProduct = new OrderProduct(productId, quantity);
                            orderProductList.add(orderProduct);
                        }
                    }
                    order.setProducts(orderProductList);
                    order.setTotalPrice(resultSet.getBigDecimal("total_price"));
                    order.setCustomerName(resultSet.getString("customer_name"));
                    order.setCustomerAddress(resultSet.getString("customer_address"));
                    order.setCustomerEmail(resultSet.getString("customer_email"));
                    order.setPaymentStatus(resultSet.getBoolean("payment_status"));
                    orderList.add(order);
                } catch (Exception e) {
                    //klaida konvertuojant json objektus i lista
                    return new ArrayList<>();
                }
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
            if(!hasResults) return BigDecimal.valueOf(-1.00);
            String result = resultSet.getString("products");
            List<OrderProduct> orderProductList = fromJsonToOrderProductList(result);
            if(orderProductList.isEmpty()) return BigDecimal.valueOf(-1.00);
            BigDecimal amount = BigDecimal.valueOf(0.00);
            for (OrderProduct item : orderProductList) {
                long productId = item.getProductId();
                int quantity = item.getQuantity();
                amount = amount.add(prRepository.getProductById(productId).getPrice().multiply(BigDecimal.valueOf(quantity)));
            }
            return amount;
        } catch (SQLException e) {
            return BigDecimal.valueOf(-1.00);
        }
    }

    public List<OrderProduct> fromJsonToOrderProductList(String jsonInput){
        try {
            List<OrderProduct> orderProductList = new ArrayList<>();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonInput);
            for (JsonNode node : rootNode) {
                Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
                while (fields.hasNext()) {
                    Map.Entry<String, JsonNode> entry = fields.next();
                    int productId = Integer.parseInt(entry.getKey());  // Get productId
                    int quantity = entry.getValue().asInt();  // Get quantity
                    OrderProduct orderProduct = new OrderProduct(productId, quantity);
                    orderProductList.add(orderProduct);
                }
            }
            return orderProductList;
        } catch (Exception e) {
            //klaida konvertuojant json objektus i lista
            return new ArrayList<>();
        }





    }


}
