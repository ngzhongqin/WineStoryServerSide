package com.winestory.serverside.handler.store;

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

    public StoreHandler(PersistenceManager persistenceManager){
        this.httpResponder = new HTTPResponder();
        this.storeJSONHelper = new StoreJSONHelper();
        this.persistenceManager=persistenceManager;
    }


    public void router(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest){
        String action = null;

        Router router = new Router(fullHttpRequest.getUri());
        Map<String,List<String>> params = router.getParameters();

        //setting action
        try {
            action = params.get("action").get(0);
        }catch (Exception e){
            logger.error("com.dashb.router ERROR for action: "+e.getMessage());
        }

        //setting wine_id
        int wine_id = -1;
        try {
            String intString = params.get("wine").get(0);
            wine_id = Integer.parseInt(intString);
            logger.info("wine_id: "+wine_id);
        }catch (Exception e){
            logger.error("ERROR for intString: "+e.getMessage());
        }

        if(params.isEmpty()){
            logger.info("No Params");
        }else{

            if("GetAll".equals(action)){
                logger.info("Action = GetAll");
                getAll(ctx, fullHttpRequest);
                return;
            }

            if("ViewWine".equals(action)){
                logger.info("Action = ViewWine");
                viewWine(ctx, fullHttpRequest,wine_id);
                return;
            }

        }
    }

    private void viewWine(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest, int wine_id) {
        logger.info("Method: viewWine wine_id:"+wine_id);

        boolean check = checkIfRequestIsEmpty(fullHttpRequest);

        WineDAO wineDAO = new WineDAO(persistenceManager);
        WineEntity wineEntity = wineDAO.getWine(wine_id);
        JSONObject replyJSON = storeJSONHelper.getJSONObject(wineEntity);
        httpResponder.respond(ctx,fullHttpRequest,replyJSON);

    }

    public void getAll(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest) {
        logger.info("Method: getAll");
        boolean check = checkIfRequestIsEmpty(fullHttpRequest);

        WineDAO wineDAO = new WineDAO(persistenceManager);
        List<WineEntity> wineEntityList = wineDAO.getAllWines();
        JSONObject replyJSON = storeJSONHelper.getJSONObject(wineEntityList);

        httpResponder.respond(ctx,fullHttpRequest,replyJSON);


    }

    private String getRequestString(FullHttpRequest fullHttpRequest){
        logger.info("getRequestString content:"+fullHttpRequest.content().toString(CharsetUtil.UTF_8));
        String reqString = fullHttpRequest.content().toString(CharsetUtil.UTF_8);
        return reqString;
    }

    private boolean checkIfRequestIsEmpty(FullHttpRequest fullHttpRequest){
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


}
