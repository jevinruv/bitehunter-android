package com.bitehunter.bitehunter.Model;

/**
 * Created by Ludowica on 19/12/2017.
 */

public class Item {
    String itemName;
    double itemPrice;
    String itemDesc;

    public Item() {
    }

    public Item(String itemName, double itemPrice, String itemDesc) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.itemDesc = itemDesc;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public String getItemDesc() {
        return itemDesc;
    }

    public void setItemDesc(String itemDesc) {
        this.itemDesc = itemDesc;
    }
}
