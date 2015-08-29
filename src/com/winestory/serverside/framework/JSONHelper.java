package com.winestory.serverside.framework;

import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.CharsetUtil;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhongqinng on 27/7/15.
 * JSONHelper
 */
public class JSONHelper {
    public Logger logger = Logger.getLogger(JSONHelper.class);

    public JSONHelper(){};

    public String getString(JSONObject jsonObject, String key){
        String returnString = null;
        if(key!=null){
            if(jsonObject!=null){
                try{
                    returnString = jsonObject.getString(key);
                }catch (Exception e){
                    logger.error("getString: Exception: "+e.getMessage());
                }
            }else{
                logger.error("getString: jsonObject:"+jsonObject+" is null");
            }
        }else{
            logger.error("getString: key:"+key+" is null");
        }

        return returnString;
    }

    public JSONObject getJSONObject(JSONObject incoming, String key){
        JSONObject returnJSONObject = null;

        try {
            returnJSONObject = (JSONObject) incoming.get(key);
        } catch (JSONException e) {
            logger.error("getJSONObject: Exception: " + e.getMessage());
        }

        return returnJSONObject;
    }

    public boolean checkIfRequestIsEmpty(FullHttpRequest fullHttpRequest){
        logger.info("checkIfRequestIsEmpty");
        String reqString = fullHttpRequest.content().toString(CharsetUtil.UTF_8);
        boolean returnBool = true;
        try {
            if(reqString!=null) {
                if(!reqString.isEmpty()) {
                    JSONObject incoming = new JSONObject(reqString);
                    returnBool = false;

                }else{
                    logger.info("INCOMING REQUEST IS EMPTY");
                }
            }
        } catch (JSONException e) {
            logger.error("incoming reqString that caused error: "+reqString);
            e.printStackTrace();
        }
        return returnBool;
    }

    public String getRequestString(FullHttpRequest fullHttpRequest){
        logger.info("getRequestString content:"+fullHttpRequest.content().toString(CharsetUtil.UTF_8));
        String reqString = fullHttpRequest.content().toString(CharsetUtil.UTF_8);
        return reqString;
    }

    public JSONArray getJSONArray(JSONObject incoming, String key) {
        JSONArray returnJSONArray = null;

        try {
            returnJSONArray = (JSONArray) incoming.get(key);
        } catch (JSONException e) {
            logger.error("getJSONArray: Exception: " + e.getMessage());
        }

        return returnJSONArray;

    }

    public int getInt(JSONObject jsonObject, String key) {
        int returnInt = -1;
        try{
            returnInt  = jsonObject.getInt(key);
        }catch (Exception e) {
            logger.error("getInt: Exception: " + e.getMessage());
        }

        return returnInt;

    }
}