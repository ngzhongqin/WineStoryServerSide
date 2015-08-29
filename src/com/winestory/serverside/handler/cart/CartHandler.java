package com.winestory.serverside.handler.cart;

import com.winestory.serverside.framework.JSONHelper;
import com.winestory.serverside.framework.UUIDGenerator;
import com.winestory.serverside.framework.VO.SessionVO;
import com.winestory.serverside.framework.VO.UserVO;
import com.winestory.serverside.framework.database.DAO.SessionDAO;
import com.winestory.serverside.framework.database.DAO.UserDAO;
import com.winestory.serverside.framework.database.PersistenceManager;
import com.winestory.serverside.framework.response.HTTPResponder;
import com.winestory.serverside.framework.security.PasswordHash;
import com.winestory.serverside.router.Router;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.CharsetUtil;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.List;
import java.util.Map;


public class CartHandler {
    public Logger logger = Logger.getLogger(CartHandler.class);
    private HTTPResponder httpResponder;
    private PersistenceManager persistenceManager;

    public CartHandler(PersistenceManager persistenceManager){
        this.httpResponder = new HTTPResponder();
        this.persistenceManager=persistenceManager;
    }

    public void router(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest){

        Router router = new Router(fullHttpRequest.getUri(),persistenceManager);
        String action = router.getAction();
        Map<String,List<String>> params = router.getParameters();
        String session_id = router.getSession();

        if(params.isEmpty()){
            logger.info("No Params");
        }else{

            if("GetDetails".equals(action)){
                logger.info("Action = GetDetails");
                getDetails(ctx, fullHttpRequest);
                return;
            }

            if("PrepCart".equals(action)){
                logger.info("Action = PrepCart");
                prepCart(ctx, fullHttpRequest);
                return;
            }

        }
    }

    private void prepCart(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest) {
        logger.info("Method: prepCart");
        logger.info("content:"+fullHttpRequest.content().toString(CharsetUtil.UTF_8));
    }

    private void getDetails(ChannelHandlerContext ctx, FullHttpRequest req){
        logger.info("Method: getDetails");
        logger.info("content:"+req.content().toString(CharsetUtil.UTF_8));

        String reqString = req.content().toString(CharsetUtil.UTF_8);

        try {
            if(reqString!=null) {
                if(!reqString.isEmpty()) {
                    JSONObject incoming = new JSONObject(reqString);

                    JSONHelper jsonHelper = new JSONHelper();

                    JSONObject data = jsonHelper.getJSONObject(incoming, "data");
                    JSONArray globalCart = jsonHelper.getJSONArray(data,"globalCart");


                    if(globalCart == null){
                        //return empty object back to client side. don't leave them hanging.
                    }else{
                        //objects are available in globalCart
                        int i = 0;
                        int size = globalCart.length();
                        while(i<size){
                            JSONObject wine = globalCart.getJSONObject(i);
                            int wine_id = jsonHelper.getInt(wine,"id");
                            int qty = jsonHelper.getInt(wine,"qty");
                            logger.info("getDetails: wine_id:"+wine_id+" qty:"+qty);
                            i++;
                        }

                    }


                }else{
                    logger.info("INCOMING REQUEST IS EMPTY!");
                }
            }
        } catch (JSONException e) {
            logger.error("incoming reqString that caused error: "+reqString);
            e.printStackTrace();
        }
    }

}
