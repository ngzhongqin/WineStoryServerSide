package com.winestory.serverside.framework.helper;

import com.winestory.serverside.framework.VO.StatusVO;

/**
 * Created by zhongqinng on 29/8/15.
 * ReturnStatusHelper
 */
public class ReturnStatusHelper {
    public ReturnStatusHelper(){}

    public StatusVO getSEC100_LoginSuccess(){
        String code = "SEC-100";
        String message = "Your login is successful.";
        String colour = "G";

        return new StatusVO(code,message,colour);
    }

    public StatusVO getSEC101_LoginFail(){
        String code = "SEC-101";
        String message = "The email and password don't match up. Sorry..";
        String colour = "R";

        return new StatusVO(code,message,colour);
    }

    public StatusVO getSEC102_LogoutSuccess(){
        String code = "SEC-102";
        String message = "You just logged out. See you soon.";
        String colour = "G";

        return new StatusVO(code,message,colour);
    }

    public StatusVO getSEC103_SignUpSuccess(){
        String code = "SEC-103";
        String message = "Hey! Thanks for signing up";
        String colour = "G";

        return new StatusVO(code,message,colour);
    }


    public StatusVO getSEC104_SignUpFail(){
        String code = "SEC-104";
        String message = "We failed to sign you up. Maybe your chosen email is already taken?";
        String colour = "R";

        return new StatusVO(code,message,colour);
    }
}
