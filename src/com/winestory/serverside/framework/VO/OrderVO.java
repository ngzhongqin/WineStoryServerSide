package com.winestory.serverside.framework.VO;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by zhongqinng on 30/8/15.
 * OrderVO
 */
public class OrderVO {
    private long id;
    private String email;
    private String full_name;
    private String address;
    private String postal_code;
    private String mobile;
    private double tax;
    private double shipping_cost;
    private double total_cost;
    private double sub_total;
    private Timestamp createddt;
    private long user_id;
    private UserVO userVO;
    private String other_instructions;
    private ArrayList<OrderItemVO> orderItemVOArrayList;
    private String order_state;
    private PaymentVO paymentVO;

    public OrderVO(){}

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public double getShipping_cost() {
        return shipping_cost;
    }

    public void setShipping_cost(double shipping_cost) {
        this.shipping_cost = shipping_cost;
    }

    public double getTotal_cost() {
        return total_cost;
    }

    public int getTotal_cost_in_cents(){
        return (int) total_cost*100;
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

    public UserVO getUserVO() {
        return userVO;
    }

    public void setUserVO(UserVO userVO) {
        this.userVO = userVO;
    }

    public long getUser_id() {
        return user_id;
    }

    public void setUser_id(long user_id) {
        this.user_id = user_id;
    }

    public ArrayList<OrderItemVO> getOrderItemVOArrayList() {
        return orderItemVOArrayList;
    }

    public void setOrderItemVOArrayList(ArrayList<OrderItemVO> orderItemVOArrayList) {
        this.orderItemVOArrayList = orderItemVOArrayList;
    }

    public String getOther_instructions() {
        return other_instructions;
    }

    public void setOther_instructions(String other_instructions) {
        this.other_instructions = other_instructions;
    }

    public double getTax() {
        return tax;
    }

    public void setTax(double tax) {
        this.tax = tax;
    }

    public double getSub_total() {
        return sub_total;
    }

    public void setSub_total(double sub_total) {
        this.sub_total = sub_total;
    }


    public String getOrder_state() {
        return order_state;
    }

    public void setOrder_state(String order_state) {
        this.order_state = order_state;
    }

    public PaymentVO getPaymentVO() {
        return paymentVO;
    }

    public void setPaymentVO(PaymentVO paymentVO) {
        this.paymentVO = paymentVO;
    }
}
