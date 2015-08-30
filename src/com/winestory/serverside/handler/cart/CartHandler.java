package com.winestory.serverside.handler.cart;

import com.winestory.serverside.framework.JSONHelper;
import com.winestory.serverside.framework.UUIDGenerator;
import com.winestory.serverside.framework.VO.OrderVO;
import com.winestory.serverside.framework.VO.SessionVO;
import com.winestory.serverside.framework.VO.UserVO;
import com.winestory.serverside.framework.database.DAO.OrderDAO;
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
        UserVO userVO = router.getUser();

        if(params.isEmpty()){
            logger.info("No Params");
        }else{

            if("PrepCart".equals(action)){
                logger.info("Action = PrepCart");
                prepCart(ctx, fullHttpRequest,userVO);
                return;
            }

        }
    }

    private void prepCart(ChannelHandlerContext channelHandlerContext,
                          FullHttpRequest fullHttpRequest,
                          UserVO userVO) {
        logger.info("Method: prepCart");
        logger.info("content:"+fullHttpRequest.content().toString(CharsetUtil.UTF_8));
        String reqString = fullHttpRequest.content().toString(CharsetUtil.UTF_8);

        try {
            if(reqString!=null) {
                if(!reqString.isEmpty()) {

                    //Get User inputs
                    JSONObject incoming = new JSONObject(reqString);
                    JSONHelper jsonHelper = new JSONHelper();
                    JSONObject data = jsonHelper.getJSONObject(incoming, "data");
                    OrderVO orderVO = new CartJSONHelper().getOrderVO(data,userVO,persistenceManager);

                    //Validate user inputs

                    //process

                    Number sub_total = getSubTotal(orderVO);
                    Number shipping_cost = getShippingCost(sub_total);
                    Number tax = getTax(sub_total);
                    Number total = sub_total.doubleValue()+shipping_cost.doubleValue()+tax.doubleValue();

                    orderVO.setSub_total(sub_total.doubleValue());
                    orderVO.setShipping_cost(shipping_cost.doubleValue());
                    orderVO.setTax(tax.doubleValue());
                    orderVO.setTotal_cost(total.doubleValue());
                    logger.info("Total: $"+total);

                    //get PayPal hidden value


                    //Save order
                    OrderDAO orderDAO = new OrderDAO(persistenceManager);
                    orderDAO.createOrder(orderVO);

                    //response

                    httpResponder.respond2(channelHandlerContext,
                            fullHttpRequest,
                            new CartJSONHelper().getPrepCartJSON(orderVO),
                            null,
                            null);

                }else{
                    logger.info("INCOMING REQUEST IS EMPTY!");
                }
            }
        } catch (JSONException e) {
            logger.error("incoming reqString that caused error: "+reqString);
            e.printStackTrace();
        }
    }

    private Number getTax(Number sub_total) {
        Number tax =sub_total.doubleValue() * 0.07;
        logger.info("getTax: $"+tax);
        return tax;
    }

    private Number getShippingCost(Number sub_total) {
        Number shipping_cost = 0;
        if(sub_total.doubleValue()<200){
            shipping_cost =  20;
        }else {
            shipping_cost =  0;
        }
        logger.info("getShippingCost: $"+shipping_cost);
        return shipping_cost;
    }

    private Number getSubTotal(OrderVO orderVO) {
        Number returnNumber = 0;
        if(orderVO!=null){
            if(orderVO.getOrderItemVOArrayList()!=null){
                int i = 0;
                int size = orderVO.getOrderItemVOArrayList().size();
                while(i<size){
                    if(orderVO.getOrderItemVOArrayList().get(i).getQuantity()>0){
                        if(orderVO.getOrderItemVOArrayList().get(i).getUnit_price().doubleValue()>0){
                            Number unitPriceTimesQuantity = orderVO.getOrderItemVOArrayList().get(i).getQuantity() *
                                    orderVO.getOrderItemVOArrayList().get(i).getUnit_price().doubleValue();
                            returnNumber = returnNumber.doubleValue() + unitPriceTimesQuantity.doubleValue();
                        }else{
                            logger.error("getSubTotal getUnit_price is 0 or less for object i:"+i);
                        }
                    }else{
                        logger.error("getSubTotal getQuantity is 0 or less for object i:"+i);
                    }
                    i++;
                }
            }else{
                logger.error("getSubTotal orderVO.getOrderItemVOArrayList is null");
            }
        }else{
            logger.error("getSubTotal orderVO is null");
        }
        logger.info("getSubTotal: $"+returnNumber);
        return returnNumber;
    }
}

