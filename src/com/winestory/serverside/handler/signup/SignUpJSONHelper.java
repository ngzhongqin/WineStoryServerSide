package com.winestory.serverside.handler.signup;

import com.winestory.serverside.framework.VO.SessionVO;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by zhongqinng on 29/8/15.
 * SignUpJSONHelper
 */
public class SignUpJSONHelper {
    public Logger logger = Logger.getLogger(SignUpJSONHelper.class);
    public SignUpJSONHelper(){}

    public JSONObject getSession(SessionVO sessionVO){
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("session_id",sessionVO.getId());
        } catch (JSONException e) {
            logger.error("getSession ERROR:" + e.getMessage());
        }

        return jsonObject;
    }
}
