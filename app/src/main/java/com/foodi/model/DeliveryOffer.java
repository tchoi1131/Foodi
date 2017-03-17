package com.foodi.model;

/**
 * Created by Tom Wong on 1/31/2017.
 * Class created to store delivery offers created by drivers
 */

public class DeliveryOffer {
    //Possible Statuses for a Delivery Offer
    public static final String DELIVERY_OFFER_STATUS_PENDING_CUSTOMER_REPLY = "Pending customer reply";
    public static final String DELIVERY_OFFER_STATUS_PENDING_DRIVER_CONFIRM = "Pending driver confirm";
    public static final String DELIVERY_OFFER_STATUS_CONFIRMED = "Confirmed";
    public static final String DELIVERY_OFFER_STATUS_REJECTED_BY_DRIVER = "Rejected by driver";
    public static final String DELIVERY_OFFER_STATUS_REJECTED_BY_CUStOMER = "Rejected by customer";

    private String driverName;                  //driver name
    private Double offerPrice;                  //offer Price
    private String estimatedDeliveryTime;       //estimated delivery time
    private String offerStatus;                 //offer status
    private String deliveryRequestKey;          //delivery request key of the corresponding delivery request created by customers

    //default constructor
    public DeliveryOffer(){
        driverName = "";
        offerPrice = 0.0;
        estimatedDeliveryTime = "";
        offerStatus = "";
    }

    /**
     * create Delivery offer using the input parameters
     * @param driverName: Driver's name
     * @param offerPrice: Price suggested by driver for the reward after delivery
     * @param estimatedDeliveryTime: Estimated delivery time suggest by the driver
     * @param offerStatus: offer status
     * @param deliveryRequestKey: the corresponding delivery request key
     */
    public DeliveryOffer(String driverName, double offerPrice, String estimatedDeliveryTime, String offerStatus, String deliveryRequestKey) {
        this.driverName = driverName;
        this.offerPrice = offerPrice;
        this.estimatedDeliveryTime = estimatedDeliveryTime;
        this.offerStatus = offerStatus;
        this.deliveryRequestKey = deliveryRequestKey;
    }

    //getters and setters
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
