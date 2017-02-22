package com.foodi.model;

import java.util.Date;

/**
 * Created by Tom Wong on 1/31/2017.
 */

public class DeliveryOffer {
    public static final String DELIVERY_OFFER_STATUS_PENDING_CUSTOMER_REPLY = "Pending customer reply";
    public static final String DELIVERY_OFFER_STATUS_PENDING_DRIVER_CONFIRM = "Pending driver confirm";
    public static final String DELIVERY_OFFER_STATUS_CONFIRMED = "Confirmed";
    public static final String DELIVERY_OFFER_STATUS_REJECTED_BY_DRIVER = "Rejected by driver";
    public static final String DELIVERY_OFFER_STATUS_REJECTED_BY_CUStOMER = "Rejected by customer";

    private String driverName;
    private Double offerPrice;
    private String estimatedDeliveryTime;
    private String offerStatus;
    private String deliveryRequestKey;

    public DeliveryOffer(){
        driverName = "";
        offerPrice = 0.0;
        estimatedDeliveryTime = "";
        offerStatus = "";
    }

    public DeliveryOffer(String driverName, double offerPrice, String estimatedDeliveryTime, String offerStatus, String deliveryRequestKey) {
        this.driverName = driverName;
        this.offerPrice = offerPrice;
        this.estimatedDeliveryTime = estimatedDeliveryTime;
        this.offerStatus = offerStatus;
        this.deliveryRequestKey = deliveryRequestKey;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public Double getOfferPrice() {
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

    public String getEstimatedDeliveryTime() {
        return estimatedDeliveryTime;
    }

    public void setEstimatedDeliveryTime(String estimatedDeliveryTime) {
        this.estimatedDeliveryTime = estimatedDeliveryTime;
    }

    public String getDeliveryRequestKey() {
        return deliveryRequestKey;
    }

    public void setDeliveryRequestKey(String deliveryRequestKey) {
        this.deliveryRequestKey = deliveryRequestKey;
    }
}
