package com.foodi.model;

/**
 * Created by Tom Wong on 1/31/2017.
 */

public class DeliveryRequest {
    public static final String DELIVERY_REQUEST_STATUS_ACCEPTING_OFFERS = "AcceptingOffers";

    private String orderNumber;
    private String driverUserId;
    private String restaurantName;
    private String restaurantAddressLine1;
    private String restaurantAddressLine2;
    private String restaurantAddressLine3;
    private String deliveryAddressLine1;
    private String deliveryAddressLine2;
    private String deliveryAddressLine3;
    private int maxReward;
    private String deliveryRequestStatus;

    public DeliveryRequest(){

    }

    public DeliveryRequest(String orderNumber, String driverUserId, String restaurantName,
                           String restaurantAddressLine1, String restaurantAddressLine2,
                           String restaurantAddressLine3, String deliveryAddressLine1,
                           String deliveryAddressLine2, String deliveryAddressLine3,
                           int maxReward, String deliveryRequestStatus) {
        this.orderNumber = orderNumber;
        this.driverUserId = driverUserId;
        this.restaurantName = restaurantName;
        this.restaurantAddressLine1 = restaurantAddressLine1;
        this.restaurantAddressLine2 = restaurantAddressLine2;
        this.restaurantAddressLine3 = restaurantAddressLine3;
        this.deliveryAddressLine1 = deliveryAddressLine1;
        this.deliveryAddressLine2 = deliveryAddressLine2;
        this.deliveryAddressLine3 = deliveryAddressLine3;
        this.maxReward = maxReward;
        this.deliveryRequestStatus = deliveryRequestStatus;
    }

    public String getDriverUserId() {
        return driverUserId;
    }

    public void setDriverUserId(String driverUserId) {
        this.driverUserId = driverUserId;
    }

    public String getOrderNumber() {
        return orderNumber;
    }


    public int getMaxReward() {
        return maxReward;
    }

    public void setMaxReward(int maxReward) {
        this.maxReward = maxReward;
    }

    public String getDeliveryOrderStatus() {
        return deliveryRequestStatus;
    }

    public void setDeliveryOrderStatus(String deliveryRequestStatus) {
        this.deliveryRequestStatus = deliveryRequestStatus;
    }

    public String getRestaurantAddressLine1() {
        return restaurantAddressLine1;
    }

    public void setRestaurantAddressLine1(String restaurantAddressLine1) {
        this.restaurantAddressLine1 = restaurantAddressLine1;
    }

    public String getRestaurantAddressLine2() {
        return restaurantAddressLine2;
    }

    public void setRestaurantAddressLine2(String restaurantAddressLine2) {
        this.restaurantAddressLine2 = restaurantAddressLine2;
    }

    public String getRestaurantAddressLine3() {
        return restaurantAddressLine3;
    }

    public void setRestaurantAddressLine3(String restaurantAddressLine3) {
        this.restaurantAddressLine3 = restaurantAddressLine3;
    }

    public String getDeliveryAddressLine1() {
        return deliveryAddressLine1;
    }

    public void setDeliveryAddressLine1(String deliveryAddressLine1) {
        this.deliveryAddressLine1 = deliveryAddressLine1;
    }

    public String getDeliveryAddressLine2() {
        return deliveryAddressLine2;
    }

    public void setDeliveryAddressLine2(String deliveryAddressLine2) {
        this.deliveryAddressLine2 = deliveryAddressLine2;
    }

    public String getDeliveryAddressLine3() {
        return deliveryAddressLine3;
    }

    public void setDeliveryAddressLine3(String deliveryAddressLine3) {
        this.deliveryAddressLine3 = deliveryAddressLine3;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }
}
