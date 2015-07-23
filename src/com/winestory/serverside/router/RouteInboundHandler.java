package com.winestory.serverside.router;

import com.winestory.serverside.handler.login.LoginHandler;
import com.winestory.serverside.handler.signup.SignUpHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.CharsetUtil;
import org.apache.log4j.Logger;


public class RouteInboundHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
    public Logger logger = Logger.getLogger(RouteInboundHandler.class);

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest) throws Exception {
        logger.info("channelRead Start");

            logger.info("FullHttpRequest: " + fullHttpRequest.toString());

            logger.info("content: " + fullHttpRequest.content().toString(CharsetUtil.UTF_8));

            UriPathHelper uriPathHelper = new UriPathHelper();
            UriPath uriPath = uriPathHelper.getUriPath(fullHttpRequest.getUri());

            switch (uriPath){
                case INVALID:
                    break;
                case LOGIN:
                    LoginHandler loginHandler = new LoginHandler();
                    loginHandler.login(ctx,fullHttpRequest);
                    break;
                case LOGOUT:
                    LoginHandler loginHandler1 = new LoginHandler();
                    loginHandler1.logout(ctx,fullHttpRequest,null);
                    break;
                case SIGNUP:
                    SignUpHandler signUpHandler = new SignUpHandler();
                    signUpHandler.signUp(ctx,fullHttpRequest);
                    break;
                default:
                    break;
            }

    }



    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}