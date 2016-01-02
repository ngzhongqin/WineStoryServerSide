package com.winestory.serverside.framework.database.Entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by zhongqinng on 23/7/15.
 * UserEntity
 */
@Entity
@Table(name = "cartItem", schema = "winestory")
public class CartItemEntity {

    private long wine_id;
    private int quantity;
    private String name;
    private long cart_id;
    private double unit_price;
    private Timestamp createddt;
    private long user_id;


    @Id
    @SequenceGenerator(name="cartItem_id_seq",
            sequenceName="cartItem_id_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="cartItem_id_seq")
    @Column(name = "id", updatable=false)
    private Long id;

    public Long getId(){return this.id;}
    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "wine_id")
    public long getWine_id() {
        return wine_id;
    }

    public void setWine_id(long wine_id) {
        this.wine_id = wine_id;
    }

    @Basic
    @Column(name = "cart_id")
    public long getCart_id() {
        return cart_id;
    }

    public void setCart_id(long cart_id) {
        this.cart_id = cart_id;
    }

    @Basic
    @Column(name = "name")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Basic
    @Column(name = "quantity")
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


    @Basic
    @Column(name = "unit_price")
    public double getUnit_price() {
        return unit_price;
    }

    public void setUnit_price(double unit_price) {
        this.unit_price = unit_price;
    }


    @Basic
    @Column(name = "createddt")
    public Timestamp getCreateddt() {
        return createddt;
    }

    public void setCreateddt(Timestamp createddt) {
        this.createddt = createddt;
    }

    @Basic
    @Column(name = "user_id")
    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }
}
