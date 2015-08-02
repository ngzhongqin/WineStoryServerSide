package com.winestory.serverside.router;

import com.winestory.serverside.exception.ExceptionHandler;
import com.winestory.serverside.framework.database.PersistenceManager;
import com.winestory.serverside.utils.ConfigUtil;
import com.winestory.serverside.vo.ServiceVO;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.CharsetUtil;

import java.lang.reflect.Method;
import org.apache.log4j.Logger;



public class RouteInboundHandler implements ChannelInboundHandler {
    public Logger logger = Logger.getLogger(RouteInboundHandler.class);

    private PersistenceManager persistenceManager;

    public RouteInboundHandler(PersistenceManager persistenceManager){
        this.persistenceManager = persistenceManager;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object arg1)
            throws Exception {
        FullHttpRequest fullHttpRequest = (FullHttpRequest) arg1;
        logger.info("channelRead Start");
        logger.info("FullHttpRequest: " + fullHttpRequest.toString());

        logger.info("content: " + fullHttpRequest.content().toString(CharsetUtil.UTF_8));
        logger.info("URI : " + fullHttpRequest.getUri());

        try {
            String uri = fullHttpRequest.getUri();
            uri = uri.substring(1);
            if(uri.contains("?")){
                uri = uri.substring(0, uri.indexOf('?'));
            }

            logger.info("URI : " + uri);
            ConfigUtil confg = new ConfigUtil();
            ServiceVO serviceVO =  confg.getServiceClass(uri);
            logger.info("serviceVo: "+serviceVO.getServiceID());
            logger.info("serviceVo: "+serviceVO.getServiceClass());

            Router router = new Router(fullHttpRequest.getUri());
            logger.info("URI: "+router.getUri());
            logger.info("PATH: "+router.getPath());
            logger.info("PARAMS: "+router.getParameters());

            if(null != serviceVO){
                Class c = Class.forName(serviceVO.getServiceClass());
                Object o1 = c.getConstructor(PersistenceManager.class).newInstance(persistenceManager);
                Method methodAll = c.getDeclaredMethod("router", new Class[]{ChannelHandlerContext.class,FullHttpRequest.class});
                methodAll.invoke(o1,ctx,fullHttpRequest);
            }else{
                ExceptionHandler exceptionHandler = new ExceptionHandler();
                exceptionHandler.handleException(ctx, fullHttpRequest,"Service Not found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ExceptionHandler exceptionHandler = new ExceptionHandler();
            exceptionHandler.handleException(ctx, fullHttpRequest,e.getMessage());
        }
    }


    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    public void handlerAdded(ChannelHandlerContext arg0) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void handlerRemoved(ChannelHandlerContext arg0) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void channelActive(ChannelHandlerContext arg0) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void channelInactive(ChannelHandlerContext arg0) throws Exception {
        // TODO Auto-generated method stub

    }



    @Override
    public void channelRegistered(ChannelHandlerContext arg0) throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void channelUnregistered(ChannelHandlerContext arg0)
            throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void channelWritabilityChanged(ChannelHandlerContext arg0)
            throws Exception {
        // TODO Auto-generated method stub

    }

    @Override
    public void userEventTriggered(ChannelHandlerContext arg0, Object arg1)
            throws Exception {
        // TODO Auto-generated method stub

    }
}