package com.winestory.serverside.framework;

import org.apache.log4j.Logger;
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
}