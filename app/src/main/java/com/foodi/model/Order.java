package com.foodi.model;

import java.util.ArrayList;

/**
 * Created by Tom Wong on 1/31/2017.
 */

public class Order {
    private String orderId;
    private String customerUserId;
    private String restaurantUserId;
    private ArrayList<OrderDetail> details;
    private double total;

    public Order(String orderId, String customerUserId, String restaurantUserId, ArrayList<OrderDetail> details, double total) {
        this.orderId = orderId;
        this.customerUserId = customerUserId;
        this.restaurantUserId = restaurantUserId;
        this.details = details;
        this.total = total;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getCustomerUserId() {
        return customerUserId;
    }

    public void setCustomerUserId(String customerUserId) {
        this.customerUserId = customerUserId;
    }

    public String getRestaurantUserId() {
        return restaurantUserId;
    }

    public void setRestaurantUserId(String restaurantUserId) {
        this.restaurantUserId = restaurantUserId;
    }

    public ArrayList<OrderDetail> getDetails() {
        return details;
    }

    public void setDetails(ArrayList<OrderDetail> details) {
        this.details = details;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getTotal() {
        return total;
    }
}
