package com.winestory.serverside.framework.VO;

import java.sql.Timestamp;

/**
 * Created by zhongqinng on 27/12/15.
 * PaymentVO
 */
public class PaymentVO {
    private long id;
    private String token;
    private double total_cost;
    private Timestamp createddt;
    private long user_id;
    private long order_id;
    private String payment_state;
    private String payment_error;
    private String stripeTxnId;
    private String stripeTxnDescription;

    public PaymentVO(){};

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public double getTotal_cost() {
        return total_cost;
    }

    public void setTotal_cost(double total_cost) {
        this.total_cost = total_cost;
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

    public long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(long order_id) {
        this.order_id = order_id;
    }

    public String getPayment_state() {
        return payment_state;
    }

    public void setPayment_state(String payment_state) {
        this.payment_state = payment_state;
    }

    public String getPayment_error() {
        return payment_error;
    }

    public void setPayment_error(String payment_error) {
        this.payment_error = payment_error;
    }

    public String getStripeTxnId() {
        return stripeTxnId;
    }

    public void setStripeTxnId(String stripeTxnId) {
        this.stripeTxnId = stripeTxnId;
    }

    public String getStripeTxnDescription() {
        return stripeTxnDescription;
    }

    public void setStripeTxnDescription(String stripeTxnDescription) {
        this.stripeTxnDescription = stripeTxnDescription;
    }

}
