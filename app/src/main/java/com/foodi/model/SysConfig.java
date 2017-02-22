package com.foodi.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Tom Wong on 1/31/2017.
 */

public class SysConfig {
    public static final String FBDB_SYS_CONFIGS = "SysConfigs";
    public static final String FBDB_ORDERS = "Orders";
    public static final String FBDB_ORDER_DETAILS = "OrderDetails";
    public static final String FBDB_FOODS = "Foods";
    public static final String FBDB_USERS = "Users";
    public static final String FBDB_DELIVERY_REQUESTS = "DeliveryRequests";
    public static final String FBDB_USER_DELIVERY_REQUESTS = "UserDeliveryRequest";
    public static final String FBDB_DELIVERY_REQUEST_USER_OFFER = "DeliveryRequestUserOffer";
    public static final String FBDB_DELIVERY_REQUEST_CONFIRMED_OFFER = "DeliveryRequestConfirmedOffer";
    public static final String FBDB_USER_DELIVERY_OFFER = "UserDeliveryOffer";
    public static final String FBDB_DELIVERY_OFFERS = "DeliveryOffers";

    public static final String DISPLAY_SHORT_DATE_FORMAT = "MMM dd";
    public static final String DISPLAY_TIME_FORMAT = "hh:mm aa";
    public static final String STORED_DATE_FORMAT = "yyyy-MM-dd hh:mm aa";

    public static String getDisplayShortDate(String dateTime) throws ParseException {
        SimpleDateFormat storedDateFormat = new SimpleDateFormat(SysConfig.STORED_DATE_FORMAT);
        SimpleDateFormat displayDateFormat = new SimpleDateFormat(SysConfig.DISPLAY_SHORT_DATE_FORMAT);

        return displayDateFormat.format(storedDateFormat.parse(dateTime));
    }

    public static String getDisplayTime(String dateTime) throws ParseException {
        SimpleDateFormat storedDateFormat = new SimpleDateFormat(SysConfig.STORED_DATE_FORMAT);
        SimpleDateFormat displayTimeFormat = new SimpleDateFormat(SysConfig.DISPLAY_TIME_FORMAT);

        return displayTimeFormat.format(storedDateFormat.parse(dateTime));
    }

    public static String convertToStoredDateTimeFormat(Date dateTime) throws ParseException {
        SimpleDateFormat storedDateFormat = new SimpleDateFormat(SysConfig.STORED_DATE_FORMAT);
        SimpleDateFormat displayTimeFormat = new SimpleDateFormat(SysConfig.DISPLAY_TIME_FORMAT);

        return storedDateFormat.format(dateTime);
    }

    private int nextFoodId;
    private int nextOrderId;

    public SysConfig(){

    }

    public SysConfig(int nextFoodId, int nextOrderId) {
        this.nextFoodId = nextFoodId;
        this.nextOrderId = nextOrderId;
    }

    public int getNextFoodId() {
        return nextFoodId;
    }

    public void setNextFoodId(int nextFoodId) {
        this.nextFoodId = nextFoodId;
    }

    public int getNextOrderId() {
        return nextOrderId;
    }

    public void setNextOrderId(int nextOrderId) {
        this.nextOrderId = nextOrderId;
    }

}
