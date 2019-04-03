package entity;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Cart {
    private String userId;
    private double totalPrice;
    private ArrayList<ShopItem> shopItems;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public ArrayList<ShopItem> getShopItems() {
        return shopItems;
    }

    public void setShopItems(ArrayList<ShopItem> shopItems) {
        this.shopItems = shopItems;
    }
}
