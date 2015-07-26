package com.winestory.serverside.framework.VO;

/**
 * Created by zhongqinng on 26/7/15.
 * User Value Object
 */
public class UserVO {
    private String full_name;
    private String email;

    public UserVO(String full_name,
                  String email){
        this.full_name = full_name;
        this.email = email;
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
}
