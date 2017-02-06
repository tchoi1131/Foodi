package com.foodi.model;

/**
 * Created by Tom Wong on 1/31/2017.
 */

public class Food {
    private String foodId;
    private String foodName;
    private String price;

    public Food(String foodId, String foodName, String price) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.price = price;
    }

    public String getFoodId() {
        return foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
