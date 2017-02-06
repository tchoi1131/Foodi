package com.foodi.model;

/**
 * Created by Tom Wong on 1/31/2017.
 */

public class DeliveryOffer {
    private String orderNumber;
    private String driverName;
    private String offerPrice;
    private int offerStatus;

    public DeliveryOffer(String orderNumber, String driverName, String offerPrice, int offerStatus) {
        this.orderNumber = orderNumber;
        this.driverName = driverName;
        this.offerPrice = offerPrice;
        this.offerStatus = offerStatus;
    }

    public String getdriverName() {
        return driverName;
    }

    public void setdriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getOfferPrice() {
        return offerPrice;
    }

    public void setOfferPrice(String offerPrice) {
        this.offerPrice = offerPrice;
    }

    public int getOfferStatus() {
        return offerStatus;
    }

    public void setOfferStatus(int offerStatus) {
        this.offerStatus = offerStatus;
    }
}
