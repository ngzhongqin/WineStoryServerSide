package com.winestory.serverside.framework.response;


import com.winestory.serverside.exception.ExceptionHandler;
import com.winestory.serverside.framework.VO.StatusVO;
import com.winestory.serverside.framework.VO.UserVO;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.util.CharsetUtil;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import static io.netty.handler.codec.http.HttpHeaders.Names.*;
import static io.netty.handler.codec.http.HttpResponseStatus.CONTINUE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HTTPResponder {
    public Logger logger = Logger.getLogger(HTTPResponder.class);

    public HTTPResponder(){
    }

    public void respond2(ChannelHandlerContext channelHandlerContext,FullHttpRequest fullHttpRequest,
                         JSONObject data, StatusVO statusVO, UserVO userVO){

        logger.info("Method: respond2");
        JSONObject mainJSONObject = new JSONObject();
        try {
            mainJSONObject.put("data",data);
            mainJSONObject.put("user",getUserJSONObject(userVO));
            mainJSONObject.put("returnStatus",getReturnStatusJSONObject(statusVO));
            contentRespond(channelHandlerContext, fullHttpRequest, mainJSONObject);
        } catch (JSONException e) {
            logger.error("respond2: ERROR: "+e.getMessage());
            ExceptionHandler exceptionHandler = new ExceptionHandler();
        }
    }

    private void contentRespond(ChannelHandlerContext channelHandlerContext,
                                FullHttpRequest fullHttpRequest,
                                JSONObject mainJSONObject){

        byte[] CONTENT = mainJSONObject.toString().getBytes(CharsetUtil.UTF_8);

        logger.info("Method: contentRespond: CONTENT:"+CONTENT);

        if (HttpHeaders.is100ContinueExpected(fullHttpRequest)) {
            channelHandlerContext.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
        }
        boolean keepAlive = HttpHeaders.isKeepAlive(fullHttpRequest);
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(CONTENT));


        //TODO: To remove the below line in SIT, UAT then Production
        response.headers().set("Access-Control-Allow-Origin", "*");


        response.headers().set("Access-Control-Allow-Headers","Origin, X-Requested-With, Content-Type, Accept");
        response.headers().set(CONTENT_TYPE, "application/json");
        response.headers().set(CONTENT_LENGTH, response.content().readableBytes());

        if (!keepAlive) {
            channelHandlerContext.write(response).addListener(ChannelFutureListener.CLOSE);
        } else {
            response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);

            logger.info("respond: "+response.content().toString(CharsetUtil.UTF_8));
            channelHandlerContext.write(response);
        }

    }


    //Going to decom this method
    public void respond(ChannelHandlerContext ctx, FullHttpRequest req, JSONObject jsonObject,UserVO userVO){
        logger.info("Method: respond");
        jsonObject = addUserVoIntoResponseJSONObject(jsonObject,userVO);

        byte[] CONTENT = jsonObject.toString().getBytes(CharsetUtil.UTF_8);

        if (HttpHeaders.is100ContinueExpected(req)) {
            ctx.write(new DefaultFullHttpResponse(HTTP_1_1, CONTINUE));
        }
        boolean keepAlive = HttpHeaders.isKeepAlive(req);
        FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK, Unpooled.wrappedBuffer(CONTENT));


        //TODO: To remove the below line in SIT, UAT then Production
        response.headers().set("Access-Control-Allow-Origin", "*");


        response.headers().set("Access-Control-Allow-Headers","Origin, X-Requested-With, Content-Type, Accept");
        response.headers().set(CONTENT_TYPE, "application/json");
        response.headers().set(CONTENT_LENGTH, response.content().readableBytes());

        if (!keepAlive) {
            ctx.write(response).addListener(ChannelFutureListener.CLOSE);
        } else {
            response.headers().set(CONNECTION, HttpHeaders.Values.KEEP_ALIVE);

            logger.info("respond: "+response.content().toString(CharsetUtil.UTF_8));
            ctx.write(response);
        }
    }

    private JSONObject getUserJSONObject(UserVO userVO){
        JSONObject userJSON = new JSONObject();
        if(userVO!=null){
            try {
                userJSON.put("full_name",userVO.getFull_name());
                userJSON.put("email",userVO.getEmail());
            } catch (JSONException e) {
                logger.error("getUserJSONObject: Error"+e.getMessage());
            }
        }else{
            logger.info("getUserJSONObject: userVO:" + userVO);
        }

        return userJSON;
    }


    //going to decom this method
    private JSONObject addUserVoIntoResponseJSONObject(JSONObject jsonObject, UserVO userVO){
        if(userVO!=null){
            JSONObject userJSON = new JSONObject();
            try {
                userJSON.put("full_name",userVO.getFull_name());
                userJSON.put("email",userVO.getEmail());
                jsonObject.put("user",userJSON);
            } catch (JSONException e) {
                logger.error("addUserVoIntoResponseJSONObject: setting userJSON exception: "+e.getMessage());
            }
        }else{
            logger.info("addUserVoIntoResponseJSONObject: userVO:"+userVO);
        }

        return jsonObject;
    }

    private JSONObject getReturnStatusJSONObject(StatusVO statusVO){
        JSONObject returnStatusJSONObject = new JSONObject();
        if(statusVO!=null){
            try {
                returnStatusJSONObject.put("code",statusVO.getCode());
                returnStatusJSONObject.put("message",statusVO.getMessage());
                returnStatusJSONObject.put("colour",statusVO.getColour());
            } catch (JSONException e) {
                logger.error("getReturnStatusJSONObject: error: "+e.getMessage());
            }
        }else{
            logger.info("getReturnStatusJSONObject: statusVO is null");
        }

        return returnStatusJSONObject;
    }

}

