package com.winestory.serverside.router;

import com.winestory.serverside.handler.login.LoginHandler;
import com.winestory.serverside.handler.signup.SignUpHandler;
import com.winestory.serverside.handler.store.StoreHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.CharsetUtil;
import org.apache.log4j.Logger;


public class RouteInboundHandler extends ChannelInboundHandlerAdapter {
    public Logger logger = Logger.getLogger(RouteInboundHandler.class);



    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {

        logger.info("channelReadComplete: " + ctx.toString());
        ctx.flush();
    }

    @Override
    public void channelRead (ChannelHandlerContext ctx, Object msg)
            throws Exception
    {
        // Handle requests as switch cases. GET, POST,...
        // This post helps you to understanding switch case usage on strings:
        // http://stackoverflow.com/questions/338206/switch-statement-with-strings-in-java

        logger.info("Start: ChannelRead()");

        if (msg instanceof FullHttpRequest)
        {
            logger.info("ChannelRead() - msg instanceof FullHttpRequest");
            FullHttpRequest fullHttpRequest = (FullHttpRequest) msg;
            logger.info("content: " + fullHttpRequest.content());
            logger.info("content: " + fullHttpRequest.content().toString(CharsetUtil.UTF_8));

            UriPathHelper uriPathHelper = new UriPathHelper();
            UriPath uriPath = uriPathHelper.getUriPath(fullHttpRequest.getUri());

            switch (uriPath) {
                case INVALID:
                    break;
                case SIGNUP:
                    SignUpHandler signUpHandler = new SignUpHandler();
                    signUpHandler.signUp(ctx, fullHttpRequest);
                    break;
                case LOGIN:
                    LoginHandler loginHandler = new LoginHandler();
                    loginHandler.login(ctx, fullHttpRequest);
                    break;
                case STORE:
                    StoreHandler storeHandler = new StoreHandler();
                    storeHandler.getStore(ctx, fullHttpRequest);
                    break;
                default:
                    break;
            }

        }


    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}