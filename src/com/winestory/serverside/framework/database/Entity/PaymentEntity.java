package com.winestory.serverside.framework.database.Entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by zhongqinng on 23/7/15.
 * PaymentEntity
 */
@Entity
@Table(name = "payment", schema = "winestory")
public class PaymentEntity {
    private String token;
    private double total_cost;
    private Timestamp createddt;
    private long user_id;
    private long order_id;
    private String payment_state;
    private String payment_error;
    private String stripe_txn_id;
    private String stripe_response;
    private String stripe_txn_description;

    @Id
    @SequenceGenerator(name="payment_id_seq",
            sequenceName="payment_id_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="payment_id_seq")
    @Column(name = "id", updatable=false)
    private Long id;

    public Long getId(){return this.id;}
    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "token")
    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
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
    @Column(name = "order_id")
    public long getOrder_id() {
        return order_id;
    }

    public void setOrder_id(long order_id) {
        this.order_id = order_id;
    }

    @Basic
    @Column(name = "payment_state")
    public String getPayment_state() {
        return payment_state;
    }

    public void setPayment_state(String payment_state) {
        this.payment_state = payment_state;
    }

    @Basic
    @Column(name = "payment_error")
    public String getPayment_error() {
        return payment_error;
    }

    public void setPayment_error(String payment_error) {
        this.payment_error = payment_error;
    }


    @Basic
    @Column(name = "stripe_txn_id")
    public String getStripe_txn_id() {
        return stripe_txn_id;
    }

    public void setStripe_txn_id(String stripe_txn_id) {
        this.stripe_txn_id = stripe_txn_id;
    }

    @Basic
    @Column(name = "stripe_txn_description")
    public String getStripe_txn_description() {
        return stripe_txn_description;
    }

    public void setStripe_txn_description(String stripe_txn_description) {
        this.stripe_txn_description = stripe_txn_description;
    }

    @Basic
    @Column(name = "stripe_response")
    public String getStripe_response() {
        return stripe_response;
    }

    public void setStripe_response(String stripe_response) {
        this.stripe_response = stripe_response;
    }
}
