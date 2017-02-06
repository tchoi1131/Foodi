package com.foodi.model;

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
    public static final String FBDB_DELIVERY_OFFERS = "DeliveryOffers";

    private int nextFoodId;
    private int nextOrderId;

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
