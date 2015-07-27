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
}
