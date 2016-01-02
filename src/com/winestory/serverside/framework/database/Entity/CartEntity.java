package com.winestory.serverside.framework.database.Entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by zhongqinng on 23/7/15.
 * UserEntity
 */
@Entity
@Table(name = "cart", schema = "winestory")
public class CartEntity {
    private String email;
    private String full_name;
    private String mobile;
    private String address;
    private String postal_code;
    private double shipping_cost;
    private double tax;
    private double total_cost;
    private double sub_total;
    private Timestamp createddt;
    private long user_id;
    private String other_instructions;
    private String order_state;

    @Id
    @SequenceGenerator(name="cart_id_seq",
            sequenceName="cart_id_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="cart_id_seq")
    @Column(name = "id", updatable=false)
    private Long id;

    public Long getId(){return this.id;}
    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "full_name")
    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "mobile")
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Basic
    @Column(name = "address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Basic
    @Column(name = "postal_code")
    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    @Basic
    @Column(name = "shipping_cost")
    public double getShipping_cost() {
        return shipping_cost;
    }

    public void setShipping_cost(double shipping_cost) {
        this.shipping_cost = shipping_cost;
    }

    @Basic
    @Column(name = "tax")
    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }


    @Basic
    @Column(name = "total_cost")
    public double getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(double total_cost) {
        this.total_cost = total_cost;
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


    @Basic
    @Column(name = "other_instructions")
    public String getOther_instructions() {
        return other_instructions;
    }

    public void setOther_instructions(String other_instructions) {
        this.other_instructions = other_instructions;
    }


    @Basic
    @Column(name = "sub_total")
    public double getSub_total() {
        return sub_total;
    }

    public void setSub_total(double sub_total) {
        this.sub_total = sub_total;
    }

    @Basic
    @Column(name = "order_state")
    public String getOrder_state() {
        return order_state;
    }

    public void setOrder_state(String order_state) {
        this.order_state = order_state;
    }
}
