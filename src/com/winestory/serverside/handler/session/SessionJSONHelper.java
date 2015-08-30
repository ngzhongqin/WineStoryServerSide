package com.winestory.serverside.handler.session;

import com.winestory.serverside.framework.JSONHelper;
import com.winestory.serverside.framework.VO.UserVO;
import com.winestory.serverside.framework.VO.WineVO;
import com.winestory.serverside.framework.database.Entity.WineEntity;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhongqinng on 28/7/15.
 * SessionJSONHelper
 */
public class SessionJSONHelper {
    public Logger logger = Logger.getLogger(SessionJSONHelper.class);
    private JSONHelper jsonHelper;
    public SessionJSONHelper(){
        jsonHelper = new JSONHelper();
    }

    public JSONObject getUserVOWithAddress(UserVO userVO){
        JSONObject jsonObject = new JSONObject();
        if(userVO!=null){
            try {
                jsonObject.put("full_name",userVO.getFull_name());
                jsonObject.put("email",userVO.getEmail());
                jsonObject.put("postal_code",userVO.getPostal_code());
                jsonObject.put("address",userVO.getAddress());
                jsonObject.put("mobile",userVO.getMobile());
            } catch (JSONException e) {
                logger.error("getUserVOwithAddress JSONException");
            }
        }
        return jsonObject;
    }
}
