package com.winestory.serverside.handler.store;

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
 * StoreHandler
 */
public class StoreHandler {
    public Logger logger = Logger.getLogger(StoreHandler.class);
    private HTTPResponder httpResponder;
    private StoreJSONHelper storeJSONHelper;
    private PersistenceManager persistenceManager;
    private JSONHelper jsonHelper;

    public StoreHandler(PersistenceManager persistenceManager){
        this.httpResponder = new HTTPResponder();
        this.storeJSONHelper = new StoreJSONHelper();
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

        int wine_id = router.getParamInt("wine");
        Map<String,List<String>> params = router.getParameters();


        if(params.isEmpty()){
            logger.info("No Params");
        }else{

            if("GetAll".equals(action)){
                logger.info("Action = GetAll");
                getAll(ctx, fullHttpRequest,userVO);
                return;
            }

            if("ViewWine".equals(action)){
                logger.info("Action = ViewWine");
                viewWine(ctx, fullHttpRequest,wine_id,userVO);
                return;
            }

        }
    }

    private void viewWine(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest, int wine_id, UserVO userVO) {
        logger.info("Method: viewWine wine_id:"+wine_id);

        boolean check = jsonHelper.checkIfRequestIsEmpty(fullHttpRequest);

        WineDAO wineDAO = new WineDAO(persistenceManager);
        WineEntity wineEntity = wineDAO.getWine(wine_id);
        JSONObject replyJSON = storeJSONHelper.getJSONObject(wineEntity);
        httpResponder.respond(ctx,fullHttpRequest,replyJSON,userVO);

    }

    public void getAll(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest, UserVO userVO) {
        logger.info("Method: getAll");
        boolean check = jsonHelper.checkIfRequestIsEmpty(fullHttpRequest);

        WineDAO wineDAO = new WineDAO(persistenceManager);
        List<WineEntity> wineEntityList = wineDAO.getAllWines();
        JSONObject replyJSON = storeJSONHelper.getJSONObject(wineEntityList);

        httpResponder.respond(ctx,fullHttpRequest,replyJSON, userVO);

    }

}
