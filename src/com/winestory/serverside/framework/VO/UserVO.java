package com.winestory.serverside.framework.VO;

/**
 * Created by zhongqinng on 26/7/15.
 * User Value Object
 */
public class UserVO {
    private Long id;
    private String full_name;
    private String email;
    private String password_salt_hash;
    private String postal_code;
    private String address;
    private String mobile;

    public UserVO(){

    }

    public UserVO(Long id,
                  String full_name,
                  String email,
                  String password_salt_hash){
        this.id=id;
        this.full_name = full_name;
        this.email = email;
        this.password_salt_hash = password_salt_hash;
    }

    public UserVO(String full_name,
                  String email,
                  String password_salt_hash){
        this.full_name = full_name;
        this.email = email;
        this.password_salt_hash = password_salt_hash;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword_salt_hash() {
        return password_salt_hash;
    }

    public void setPassword_salt_hash(String password_salt_hash) {
        this.password_salt_hash = password_salt_hash;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
