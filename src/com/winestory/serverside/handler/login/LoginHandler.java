package com.winestory.serverside.handler.login;

import com.winestory.serverside.framework.VO.UserVO;
import com.winestory.serverside.framework.database.DAO.UserDAO;
import com.winestory.serverside.framework.response.HTTPResponder;
import com.winestory.serverside.framework.security.PasswordHash;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.CharsetUtil;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;


public class LoginHandler {
    public Logger logger = Logger.getLogger(LoginHandler.class);
    private HTTPResponder httpResponder;

    public LoginHandler(){
        this.httpResponder = new HTTPResponder();
    }

    public void login(ChannelHandlerContext ctx, FullHttpRequest req){
        logger.info("Method: login");
        logger.info("content:"+req.content().toString(CharsetUtil.UTF_8));

        String reqString = req.content().toString(CharsetUtil.UTF_8);

        try {
            if(reqString!=null) {
                if(!reqString.isEmpty()) {
                    JSONObject incoming = new JSONObject(reqString);
                    JSONObject data = (JSONObject) incoming.get("data");

                    String email = (String) data.get("email");
                    String password = (String) data.get("password");

                    UserDAO userDAO = new UserDAO();
                    UserVO userVO = userDAO.getUser(email);

                    PasswordHash passwordHash = new PasswordHash();
                    try {
                        boolean isPasswordCorrect = passwordHash.validatePassword(password,userVO.getPassword_salt_hash());
                        logger.info("login: email:"+email+" isPasswordCorrect="+isPasswordCorrect);
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    } catch (InvalidKeySpecException e) {
                        e.printStackTrace();
                    }

                    respond(ctx, req);


                }else{
                    logger.info("INCOMING REQUEST IS EMPTY!");
                }
            }
        } catch (JSONException e) {
            logger.error("incoming reqString that caused error: "+reqString);
            e.printStackTrace();
        }
    }

    private void respond(ChannelHandlerContext ctx, FullHttpRequest req){
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("email","123456@sf.com");
            jsonObject.put("full_name","Zhong Qin");
            jsonObject.put("password", "password_input");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        httpResponder.respond(ctx,req,jsonObject);

    }

}
