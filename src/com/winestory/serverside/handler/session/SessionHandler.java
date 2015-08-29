package com.winestory.serverside.handler.session;

import com.winestory.serverside.framework.JSONHelper;
import com.winestory.serverside.framework.VO.UserVO;
import com.winestory.serverside.framework.database.DAO.WineDAO;
import com.winestory.serverside.framework.database.Entity.WineEntity;
import com.winestory.serverside.framework.database.PersistenceManager;
import com.winestory.serverside.framework.response.HTTPResponder;
import com.winestory.serverside.router.Router;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.CharsetUtil;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Created by zhongqinng on 28/7/15.
 * SessionHandler
 */
public class SessionHandler {
    public Logger logger = Logger.getLogger(SessionHandler.class);
    private HTTPResponder httpResponder;
    private PersistenceManager persistenceManager;
    private JSONHelper jsonHelper;

    public SessionHandler(PersistenceManager persistenceManager){
        this.httpResponder = new HTTPResponder();
        this.persistenceManager=persistenceManager;
        this.jsonHelper=new JSONHelper();
    }


    public void router(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest){

        Router router = new Router(fullHttpRequest.getUri(),persistenceManager);
        String action = router.getAction();
        UserVO userVO = router.getUser();

        if(userVO!=null){
            logger.info("router: action:"+action+" userVO:"+userVO.getFull_name());
        }

//        int wine_id = router.getParamInt("wine");
        Map<String,List<String>> params = router.getParameters();


        if(params.isEmpty()){
            logger.info("No Params");
        }else{

            if("GetCurrentUser".equals(action)){
                logger.info("Action = GetCurrentUser");
                getCurrentUser(ctx, fullHttpRequest, userVO);
                return;
            }

        }
    }

    public void getCurrentUser(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest, UserVO userVO) {
        logger.info("Method: getCurrentUser");
        boolean check = jsonHelper.checkIfRequestIsEmpty(fullHttpRequest);
        JSONObject replyJSON = new JSONObject();
        httpResponder.respond(ctx,fullHttpRequest,replyJSON, userVO);

    }

}
