package com.winestory.serverside.router;

import io.netty.handler.codec.http.QueryStringDecoder;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;

/**
 * Created by zhongqinng on 29/7/15.
 * Router
 */
public class Router {
    public Logger logger = Logger.getLogger(Router.class);
    private QueryStringDecoder queryStringDecoder;

    public Router(String uri){
        this.queryStringDecoder = new QueryStringDecoder(uri);
    };

    public String getUri(){
        return this.queryStringDecoder.uri();
    }

    public String getPath(){
        return this.queryStringDecoder.path();
    }

    public Map<String, List<String>> getParameters(){
        return this.queryStringDecoder.parameters();
    }

    public int getParamInt(String paraString){
        Map<String,List<String>> params = getParameters();
        int returnInt = -1;
        try {
            String intString = params.get(paraString).get(0);
            returnInt = Integer.parseInt(intString);
            logger.info("returnInt: "+returnInt);
        }catch (Exception e){
            logger.error("ERROR for intString: "+e.getMessage());
        }

        return returnInt;
    }


    public String getAction() {
        String actionString = null;
        //setting action
        try {
            actionString = getParameters().get("action").get(0);
        }catch (Exception e){
            logger.error("getAction ERROR: "+e.getMessage());
        }

        return actionString;
    }
}
