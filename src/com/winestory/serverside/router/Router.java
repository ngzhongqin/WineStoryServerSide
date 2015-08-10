package com.winestory.serverside.router;

import com.winestory.serverside.framework.VO.UserVO;
import com.winestory.serverside.framework.database.DAO.SessionDAO;
import com.winestory.serverside.framework.database.DAO.UserDAO;
import com.winestory.serverside.framework.database.PersistenceManager;
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
    private PersistenceManager persistenceManager;

    public Router(String uri, PersistenceManager persistenceManager){
        this.queryStringDecoder = new QueryStringDecoder(uri);
        this.persistenceManager = persistenceManager;
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

    private String getSession() {
        String sessionString = null;
        //setting action
        try {
            sessionString = getParameters().get("session_id").get(0);
        }catch (Exception e){
            logger.error("sessionString ERROR: "+e.getMessage());
        }

        return sessionString;
    }

    public UserVO getUser(){
        String session_id = getSession();
        UserDAO userDAO = new UserDAO(persistenceManager);
        return userDAO.getUserFromSessionId(session_id);

    }
}
