package com.foodi.model;

import java.util.Date;

/**
 * Created by Tom Wong on 1/31/2017.
 */

public class DeliveryOffer {
    public static final String DELIVERY_Offer_STATUS_WAITING_FOR_REPLY = "Waiting for reply";
    public static final String DELIVERY_Offer_STATUS_CUSTOMER_CONFIRMED = "Customer Confirmed";

    private String orderNumber;
    private String driverName;
    private double offerPrice;
    private Date estimatedDeliveryTime;
    private String offerStatus;

    public DeliveryOffer(){

    }

    public DeliveryOffer(String orderNumber, String driverName, double offerPrice, Date estimatedDeliveryTime, String offerStatus) {
        this.orderNumber = orderNumber;
        this.driverName = driverName;
        this.offerPrice = offerPrice;
        this.estimatedDeliveryTime = estimatedDeliveryTime;
        this.offerStatus = offerStatus;
    }

    public String getdriverName() {
        return driverName;
    }

    public void setdriverName(String driverName) {
        this.driverName = driverName;
    }

    public double getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(double offerPrice) {
        this.offerPrice = offerPrice;
    }

    public String getOfferStatus() {
        return offerStatus;
    }

    public void setOfferStatus(String offerStatus) {
        this.offerStatus = offerStatus;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Date getEstimatedDeliveryTime() {
        return estimatedDeliveryTime;
    }

    public void setEstimatedDeliveryTime(Date estimatedDeliveryTime) {
        this.estimatedDeliveryTime = estimatedDeliveryTime;
    }
}
