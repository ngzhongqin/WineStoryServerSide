package com.winestory.serverside.framework.VO;

/**
 * Created by zhongqinng on 29/8/15.
 * StatusVO
 */
public class StatusVO {
    private String code;
    private String message;
    private String colour;

    public StatusVO(String code, String message, String colour){
        this.code=code;
        this.colour=colour;
        this.message=message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }
}
