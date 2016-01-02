package com.winestory.serverside.framework.VO;

import java.sql.Timestamp;

/**
 * Created by zhongqinng on 30/8/15.
 * OrderItemVO
 */
public class CartItemVO {
    private long id;
    private String name;
    private Number unit_price;
    private int quantity;
    private long cart_id;
    private Timestamp createddt;
    private long user_id;
    private long wine_id;
    private WineVO wineVO;

    public CartItemVO(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Number getUnit_price() {
        return unit_price;
    }

    public double getUnit_price_double(){
        return (double) unit_price;
    }

    public void setUnit_price(Number unit_price) {
        this.unit_price = unit_price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long getCart_id() {
        return cart_id;
    }

    public void setCart_id(long cart_id) {
        this.cart_id = cart_id;
    }

    public Timestamp getCreateddt() {
        return createddt;
    }

    public void setCreateddt(Timestamp createddt) {
        this.createddt = createddt;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public long getWine_id() {
        return wine_id;
    }

    public void setWine_id(long wine_id) {
        this.wine_id = wine_id;
    }

    public WineVO getWineVO() {
        return wineVO;
    }

    public void setWineVO(WineVO wineVO) {
        this.wineVO = wineVO;
    }
}
