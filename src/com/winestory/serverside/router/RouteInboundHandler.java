package com.winestory.serverside.router;

import com.winestory.serverside.framework.response.HTTPResponder;
import com.winestory.serverside.handler.login.LoginHandler;
import com.winestory.serverside.handler.signup.SignUpHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.util.CharsetUtil;
import io.netty.util.ReferenceCountUtil;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;


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
                default:
                    break;
            }


//            respond(ctx,fullHttpRequest);
        }


    }


//    @Override
//    protected void channelRead0(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest) throws Exception {
//
//        logger.info("channelRead Start");
//
//        logger.info("FullHttpRequest: " + fullHttpRequest.toString());
//
//        logger.info("fullHttpRequest.content().isReadable()===" + fullHttpRequest.content().isReadable());
//
//        logger.info("content: " + fullHttpRequest.content().toString(CharsetUtil.UTF_8));

//            UriPathHelper uriPathHelper = new UriPathHelper();
//            UriPath uriPath = uriPathHelper.getUriPath(fullHttpRequest.getUri());
//
//            switch (uriPath) {
//                case INVALID:
//                    break;
//                case LOGIN:
//                    LoginHandler loginHandler = new LoginHandler();
//                    loginHandler.login(ctx, fullHttpRequest);
//                    break;
//                case LOGOUT:
//                    LoginHandler loginHandler1 = new LoginHandler();
//                    loginHandler1.logout(ctx, fullHttpRequest, null);
//                    break;
//                case SIGNUP:
//                    SignUpHandler signUpHandler = new SignUpHandler();
//                    signUpHandler.signUp(ctx, fullHttpRequest);
//                    break;
//                default:
//                    break;
//            }
//
//
//        logger.info(" ");
//        logger.info(" ");
//        logger.info(" ");
//        logger.info(" ");
//        logger.info(" ");
//        logger.info(" ");
//        logger.info(" ");
//        logger.info(" ");

        //ReferenceCountUtil.release(fullHttpRequest);

//    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}