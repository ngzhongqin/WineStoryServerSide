package com.winestory.serverside.handler.order;

import com.winestory.serverside.framework.JSONHelper;
import com.winestory.serverside.framework.VO.OrderVO;
import com.winestory.serverside.framework.VO.StatusVO;
import com.winestory.serverside.framework.VO.UserVO;
import com.winestory.serverside.framework.constants.OrderState;
import com.winestory.serverside.framework.database.DAO.OrderDAO;
import com.winestory.serverside.framework.database.PersistenceManager;
import com.winestory.serverside.framework.payment.PaymentConstant;
import com.winestory.serverside.framework.payment.StripePayment;
import com.winestory.serverside.framework.response.HTTPResponder;
import com.winestory.serverside.router.Router;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.CharsetUtil;
import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhongqinng on 27/12/15.
 * Handle Orders
 */
public class OrderHandler {
    public Logger logger = Logger.getLogger(OrderHandler.class);
    private HTTPResponder httpResponder;
    private PersistenceManager persistenceManager;

    public OrderHandler(PersistenceManager persistenceManager){
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

            if(OrderConstants.ACTION_SUBMIT_ORDER.equals(action)){
                logger.info("Action:"+ OrderConstants.ACTION_SUBMIT_ORDER);
                submitOrder(ctx, fullHttpRequest, userVO);
                return;
            }

            if(OrderConstants.ACTION_GET_ALL_USER_ORDERS.equals(action)){
                logger.info("Action:"+ OrderConstants.ACTION_GET_ALL_USER_ORDERS);
                getAllUserOrders(ctx, fullHttpRequest, userVO);
                return;
            }

        }
    }

    private void getAllUserOrders(ChannelHandlerContext channelHandlerContext,
                                  FullHttpRequest fullHttpRequest,
                                  UserVO userVO) {
        logger.info("Method:"+ OrderConstants.ACTION_GET_ALL_USER_ORDERS);

        logger.info("content:"+fullHttpRequest.content().toString(CharsetUtil.UTF_8));
        String reqString = fullHttpRequest.content().toString(CharsetUtil.UTF_8);

        try {
            if(reqString!=null) {
                if(!reqString.isEmpty()) {

                    //Get User inputs
                    JSONObject incoming = new JSONObject(reqString);
                    JSONHelper jsonHelper = new JSONHelper();
                    JSONObject data = jsonHelper.getJSONObject(incoming, "data");

                    OrderDAO orderDAO = new OrderDAO(persistenceManager);
                    ArrayList<OrderVO> orderVOArrayList = orderDAO.getUserOrderVOArrayList(userVO);

                    //response

                    httpResponder.respond2(channelHandlerContext,
                            fullHttpRequest,
                            new OrderJSONHelper().getAllUserOrdersResponse(orderVOArrayList),
                            getAllUserOrdersStatus(orderVOArrayList),
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

    private StatusVO getAllUserOrdersStatus(ArrayList<OrderVO> orderVOArrayList) {
        if(orderVOArrayList!=null){
            return new StatusVO(OrderConstants.CODE_GET_ALL_USER_ORDERS_SUCC,
                    OrderConstants.MSG_GET_ALL_USER_ORDERS_SUCC,
                    OrderConstants.COLOUR_GET_ALL_USER_ORDERS_SUCC);
        }else{
            return new StatusVO(OrderConstants.CODE_GET_ALL_USER_ORDERS_FAIL,
                    OrderConstants.MSG_GET_ALL_USER_ORDERS_FAIL,
                    OrderConstants.COLOUR_GET_ALL_USER_ORDERS_FAIL);
        }
    }

    private void submitOrder(ChannelHandlerContext channelHandlerContext,
                          FullHttpRequest fullHttpRequest,
                          UserVO userVO) {
        logger.info("Method:"+ OrderConstants.ACTION_SUBMIT_ORDER);

        logger.info("content:"+fullHttpRequest.content().toString(CharsetUtil.UTF_8));
        String reqString = fullHttpRequest.content().toString(CharsetUtil.UTF_8);

        try {
            if(reqString!=null) {
                if(!reqString.isEmpty()) {

                    //Get User inputs
                    JSONObject incoming = new JSONObject(reqString);
                    JSONHelper jsonHelper = new JSONHelper();
                    JSONObject data = jsonHelper.getJSONObject(incoming, "data");
                    OrderVO orderVO = new OrderJSONHelper().getOrderVO(data, userVO, persistenceManager);

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

                    //Save order
                    OrderDAO orderDAO = new OrderDAO(persistenceManager);
                    orderVO = orderDAO.createOrder(orderVO);

                    //charging to Token
                    StripePayment stripePayment = new StripePayment(persistenceManager);
                    orderVO = stripePayment.charge(orderVO);

                    //response

                    httpResponder.respond2(channelHandlerContext,
                            fullHttpRequest,
                            new OrderJSONHelper().getSubmitOrderResponse(orderVO),
                            getSubmitOrderStatus(orderVO),
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

    private StatusVO getSubmitOrderStatus(OrderVO orderVO){
        if(OrderState.CHARGE_SUCC.equals(orderVO.getOrder_state())){
            return new StatusVO(OrderConstants.CODE_ORDER_SUCC, OrderConstants.MSG_ORDER_SUCC, OrderConstants.COLOUR_ORDER_SUCC);
        }else{
            String message = OrderConstants.MSG_ORDER_FAIL;
            if(orderVO!=null){
                if(orderVO.getPaymentVO()!=null){
                    if(orderVO.getPaymentVO().getPayment_error()!=null){
                        if(!orderVO.getPaymentVO().getPayment_error().isEmpty()){
                            message=orderVO.getPaymentVO().getPayment_error();
                        }
                    }
                }
            }
            return new StatusVO(OrderConstants.CODE_ORDER_FAIL, message, OrderConstants.COLOUR_ORDER_FAIL);
        }
    }

    private Number getTax(Number sub_total) {
        Number tax =sub_total.doubleValue() * PaymentConstant.gst;
        logger.info("getTax: $"+tax);
        return tax;
    }

    private Number getShippingCost(Number sub_total) {
        Number shipping_cost = 0;
        if(sub_total.doubleValue()<200){
            shipping_cost =  PaymentConstant.shipping_cost;
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
