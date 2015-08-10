package com.winestory.serverside.exception;

import com.winestory.serverside.framework.response.HTTPResponder;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ExceptionHandler {
	
	 private HTTPResponder httpResponder;

	    public ExceptionHandler(){
	        this.httpResponder = new HTTPResponder();
	    }

	public void handleException(ChannelHandlerContext ctx,FullHttpRequest fullHttpRequest,String execMessage) {
		 JSONObject jsonObjectmain = new JSONObject();
        try {
        	JSONObject jsonObject = new JSONObject();
            JSONArray jsonArray = new JSONArray();
            jsonObject.put("Exception",execMessage);
            jsonArray.put(jsonObject);
            jsonObjectmain.put("data", jsonArray);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        httpResponder.respond(ctx,fullHttpRequest,jsonObjectmain,null);
	}

}
