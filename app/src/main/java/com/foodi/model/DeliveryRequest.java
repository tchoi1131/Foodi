package com.foodi.model;

/**
 * Created by Tom Wong on 1/31/2017.
 * This is the class created to store Delivery Request Information.
 */

public class DeliveryRequest {
    public static final String DELIVERY_REQUEST_STATUS_ACCEPTING_OFFERS = "Accepting offers";           //The status value when it is accepting offers from drivers
    public static final String DELIVERY_REQUEST_STATUS_DELIVERY_IN_PROGRESS = "Delivery in progress";   //The status value when delivery in progress
    public static final String DELIVERY_REQUEST_STATUS_DELIVERED = "Delivered";                         //The status when the food is delivered.

    private String requestDateTime;                 //Request creation time
    private String orderNumber;                     //order number of the food order in the restaurant
    private String driverUserId;                    //User Id of the driver delivering the food
    private String restaurantName;                  //Restaurant Name
    private String restaurantAddressLine;           //Address of the restaurant
    private String restaurantAddressCity;           //City of the restaurant
    private Double restaurantAddressLat;            //Latitude of the restaurant
    private Double restaurantAddressLng;            //Longitude of the restaurant
    private String deliveryAddressLine;             //Delivery Addresss
    private String deliveryAddressCity;             //City of the delivery address
    private Double deliveryAddressLat;              //Latitude of the Delivery addresss
    private Double deliveryAddressLng;              //Longitude of the delivery address
    private int maxReward;                          //maximum reward for this request (reserve for future use)
    private String deliveryRequestStatus;           //Status of the delivery request

    //default constructor
    public DeliveryRequest(){

    }

    /**
     * Constructor to setup all the field at one time.
     * @param requestDateTime
     * @param orderNumber
     * @param driverUserId
     * @param restaurantName
     * @param restaurantAddressLine
     * @param restaurantAddressCity
     * @param restaurantAddressLat
     * @param restaurantAddressLng
     * @param deliveryAddressLine
     * @param deliveryAddressCity
     * @param deliveryAddressLat
     * @param deliveryAddressLng
     * @param maxReward
     * @param deliveryRequestStatus
     */
    public DeliveryRequest(String requestDateTime, String orderNumber, String driverUserId, String restaurantName,
                           String restaurantAddressLine, String restaurantAddressCity, Double restaurantAddressLat,
                           Double restaurantAddressLng, String deliveryAddressLine, String deliveryAddressCity,
                           Double deliveryAddressLat, Double deliveryAddressLng, int maxReward, String deliveryRequestStatus) {
        this.requestDateTime = requestDateTime;
        this.orderNumber = orderNumber;
        this.driverUserId = driverUserId;
        this.restaurantName = restaurantName;
        this.restaurantAddressLine = restaurantAddressLine;
        this.restaurantAddressCity = restaurantAddressCity;
        this.restaurantAddressLat = restaurantAddressLat;
        this.restaurantAddressLng = restaurantAddressLng;
        this.deliveryAddressLine = deliveryAddressLine;
        this.deliveryAddressCity = deliveryAddressCity;
        this.deliveryAddressLat = deliveryAddressLat;
        this.deliveryAddressLng = deliveryAddressLng;
        this.maxReward = maxReward;
        this.deliveryRequestStatus = deliveryRequestStatus;
    }

    //getters and setters
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

    public String getDeliveryRequestStatus() {
        return deliveryRequestStatus;
    }

    public void setDeliveryRequestStatus(String deliveryRequestStatus) {
        this.deliveryRequestStatus = deliveryRequestStatus;
    }

    public String getRestaurantAddressLine() {
        return restaurantAddressLine;
    }

    public void setRestaurantAddressLine(String restaurantAddressLine) {
        this.restaurantAddressLine = restaurantAddressLine ;
    }

    public String getDeliveryAddressLine() {
        return deliveryAddressLine;
    }

    public void setDeliveryAddressLine(String deliveryAddressLine) {
        this.deliveryAddressLine = deliveryAddressLine ;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRequestDateTime() {
        return requestDateTime;
    }

    public void setRequestDateTime(String requestTime) {
        this.requestDateTime = requestTime;
    }

    public String getRestaurantAddressCity() {
        return restaurantAddressCity;
    }

    public void setRestaurantAddressCity(String restaurantAddressCity) {
        this.restaurantAddressCity = restaurantAddressCity;
    }

    public String getDeliveryAddressCity() {
        return deliveryAddressCity;
    }

    public void setDeliveryAddressCity(String deliveryAddressCity) {
        this.deliveryAddressCity = deliveryAddressCity;
    }

    public Double getRestaurantAddressLat() {
        return restaurantAddressLat;
    }

    public Double getRestaurantAddressLng(){
        return restaurantAddressLng;
    }

    public void setRestaurantAddressLat(Double restaurantAddressLat) {
        this.restaurantAddressLat = restaurantAddressLat;
    }

    public void setRestaurantAddressLng(Double restaurantAddressLng) {
        this.restaurantAddressLng = restaurantAddressLng;
    }

    public Double getDeliveryAddressLat() {
        return deliveryAddressLat;
    }

    public Double getDeliveryAddressLng() {
        return deliveryAddressLng;
    }

    public void setDeliveryAddressLat(Double deliveryAddressLat) {
        this.deliveryAddressLat = deliveryAddressLat;
    }

    public void setDeliveryAddressLng(Double deliveryAddressLng) {
        this.deliveryAddressLng = deliveryAddressLng;
    }
}
