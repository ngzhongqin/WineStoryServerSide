package com.winestory.serverside.handler.login;


import com.winestory.serverside.framework.response.HTTPResponder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpRequest;
import org.apache.log4j.Logger;

public class LoginHandler {
    public Logger logger = Logger.getLogger(LoginHandler.class);
    private HTTPResponder httpResponder;

    public LoginHandler(){
         this.httpResponder = new HTTPResponder();
    }

    public void login(ChannelHandlerContext ctx, FullHttpRequest req){
        logger.info("Method: login");

    }

    public void logout(ChannelHandlerContext ctx, HttpRequest req, String accessToken){
    }
}
