package com.foodi.model;

/**
 * Created by Choi on 1/31/2017.
 */

public class OrderDetail {
    private String orderId;
    private String foodId;
    private int quantity;
    private float discount;
    private double subTotal;

    public OrderDetail(String orderId, String foodId, int quantity, float discount, double subTotal) {
        this.orderId = orderId;
        this.foodId = foodId;
        this.quantity = quantity;
        this.discount = discount;
        this.subTotal = subTotal;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getFoodId() {
        return foodId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getDiscount() {
        return discount;
    }

    public void setDiscount(float discount) {
        this.discount = discount;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }
}
