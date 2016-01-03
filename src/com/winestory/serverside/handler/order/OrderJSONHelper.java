package com.winestory.serverside.handler.order;

import com.winestory.serverside.framework.JSONHelper;
import com.winestory.serverside.framework.VO.*;
import com.winestory.serverside.framework.constants.OrderState;
import com.winestory.serverside.framework.constants.OrderStateTranslate;
import com.winestory.serverside.framework.database.DAO.WineDAO;
import com.winestory.serverside.framework.database.PersistenceManager;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zhongqinng on 27/12/15.
 * OrderJSONHelper
 */
public class OrderJSONHelper {
    public Logger logger = Logger.getLogger(OrderJSONHelper.class);
    private JSONHelper jsonHelper;
    public OrderJSONHelper(){
        jsonHelper = new JSONHelper();
    }

    public OrderVO getOrderVO(JSONObject data,UserVO userVO, PersistenceManager persistenceManager){
        OrderVO orderVO = new OrderVO();
        JSONObject checkout = jsonHelper.getJSONObject(data, "checkout");
        String full_name = jsonHelper.getString(checkout, "full_name");
        String email = jsonHelper.getString(checkout, "email");
        String address = jsonHelper.getString(checkout,"address");
        String postal_code = jsonHelper.getString(checkout,"postal_code");
        String mobile = jsonHelper.getString(checkout,"mobile");
        String other_instructions = jsonHelper.getString(checkout,"other_instructions");



        orderVO.setFull_name(full_name);
        orderVO.setEmail(email);
        orderVO.setAddress(address);
        orderVO.setPostal_code(postal_code);
        orderVO.setMobile(mobile);
        orderVO.setOther_instructions(other_instructions);


        orderVO.setUserVO(userVO);
        if(userVO==null){
            orderVO.setId(-1);
        }else{
            logger.info("userVO.getId():"+userVO.getId());
            orderVO.setUser_id(userVO.getId());
        }

        JSONObject ngCart = jsonHelper.getJSONObject(data,"ngCart");
        JSONObject cart = jsonHelper.getJSONObject(ngCart, "$cart");
        JSONArray items = jsonHelper.getJSONArray(cart, "items");
        ArrayList<OrderItemVO> orderItemVOArrayList = getOrderItemVO(items, persistenceManager);
        orderVO.setOrderItemVOArrayList(orderItemVOArrayList);

        String token = jsonHelper.getString(data, "token");


        PaymentVO paymentVO = new PaymentVO();
        paymentVO.setToken(token);

        orderVO.setPaymentVO(paymentVO);


        logger.info("OrderVO full_name: " + full_name + " email:" + email + " address:" + address
                + " postal_code:" + postal_code + " mobile:" + mobile
                + " other_instructions:" + other_instructions + " token:"+token);


        return orderVO;
    }

    private ArrayList<OrderItemVO> getOrderItemVO(JSONArray items, PersistenceManager persistenceManager){
        ArrayList<OrderItemVO> orderItemVOArrayList = new ArrayList<OrderItemVO>();
        WineDAO wineDAO = new WineDAO(persistenceManager);
        int size = items.length();
        int i = 0;
        while(i<size){
            try {
                //Only getting the ID and quantity from client side.
                int wine_id = jsonHelper.getInt(items.getJSONObject(i),"_id");
                int quantity = jsonHelper.getInt(items.getJSONObject(i),"_quantity");

                //The cost and details are gotten from the database.
                WineVO wineVO = wineDAO.getWineVO(wine_id);
                if(wineVO!=null){
                    OrderItemVO orderItemVO = new OrderItemVO();
                    orderItemVO.setName(wineVO.getName());
                    orderItemVO.setUnit_price(wineVO.getPrice());
                    orderItemVO.setWine_id(wine_id);
                    orderItemVO.setWineVO(wineVO);
                    if(quantity>0){
                        orderItemVO.setQuantity(quantity);
                    }else{
                        logger.error("getOrderItemVO; (Why is quantity less than 0) quantity:"+quantity);
                    }

                    orderItemVOArrayList.add(orderItemVO);
                }else{
                    logger.error("getOrderItemVO; Unable to get wineVO from wine_id:"+wine_id);
                }
            } catch (JSONException e) {
                logger.error("getOrderItemVO; Unable to get wine_id or quantity from object i:"+i);
            }
            i++;
        }
        return orderItemVOArrayList;
    }

    public JSONObject getSubmitOrderResponse(OrderVO orderVO) {
        return loadOrderVOIntoJSON(orderVO);
    }

    public JSONObject getAllUserOrdersResponse(ArrayList<OrderVO> orderVOArrayList) {
        return getJSONObject(orderVOArrayList);
    }

    public JSONObject getJSONObject(ArrayList<OrderVO> orderVOArrayList){
        JSONArray jsonArray = new JSONArray();
        int size = orderVOArrayList.size();
        int i = 0;
        while(i<size){
            JSONObject orderJSON = loadOrderVOIntoJSON(orderVOArrayList.get(i));
            jsonArray.put(orderJSON);
            i++;
        }
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("orders", jsonArray);

        } catch (JSONException e) {
            logger.error("getJSONObject: error:"+e.getMessage());
            e.printStackTrace();
        }

        return jsonObject;
    }

    private JSONObject loadOrderVOIntoJSON(OrderVO orderVO){
        JSONObject orderJSON = new JSONObject();
        try {
            orderJSON.put("id",orderVO.getId());
            orderJSON.put("order_state",translateOrderState(orderVO.getOrder_state()));
            orderJSON.put("tax",orderVO.getTax());
            orderJSON.put("total_cost",orderVO.getTotal_cost());
            orderJSON.put("sub_total",orderVO.getSub_total());
            orderJSON.put("shipping_cost",orderVO.getShipping_cost());
            orderJSON.put("createddt",orderVO.getCreateddt());
            orderJSON.put("full_name",orderVO.getFull_name());
            orderJSON.put("email",orderVO.getEmail());
            orderJSON.put("address",orderVO.getAddress());
            orderJSON.put("city",OrderConstants.CITY_SINGAPORE);
            orderJSON.put("country",OrderConstants.COUNTRY_SINGAPORE);
            orderJSON.put("postal_code",orderVO.getPostal_code());
            orderJSON.put("mobile",orderVO.getMobile());
            orderJSON.put("other_instructions",orderVO.getOther_instructions());

            if(orderVO.getOrderItemVOArrayList()!=null){
                JSONArray orderItems = new JSONArray();
                int i = 0;
                int size = orderVO.getOrderItemVOArrayList().size();
                while(i<size){
                    JSONObject orderItemJSON = loadOrderItemVOIntoJSON(orderVO.getOrderItemVOArrayList().get(i));
                    orderItems.put(orderItemJSON);
                    i++;
                }
                orderJSON.put("orderItems",orderItems);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return orderJSON;
    }

    private JSONObject loadOrderItemVOIntoJSON(OrderItemVO orderItemVO){
        JSONObject orderItemJSON = new JSONObject();
        try {
            orderItemJSON.put("id",orderItemVO.getWine_id());
            orderItemJSON.put("name",orderItemVO.getName());
            orderItemJSON.put("quantity",orderItemVO.getQuantity());
            orderItemJSON.put("unit_price",orderItemVO.getUnit_price());

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return orderItemJSON;
    }

    private String translateOrderState(String orderState){
        if(OrderState.DELIVERY_SUCC.equals(orderState)){
            return OrderStateTranslate.COMPLETED;
        }
        if(OrderState.DELIVERY_PEND.equals(orderState)){
            return OrderStateTranslate.PENDING_DELIVERY;
        }
        if(OrderState.CHARGE_SUCC.equals(orderState)){
            return OrderStateTranslate.PENDING_DELIVERY;
        }
        if(OrderState.ABANDON.equals(orderState)){
            return OrderStateTranslate.INCOMPLETE;
        }
        if(OrderState.CHARGE_FAIL.equals(orderState)){
            return OrderStateTranslate.INCOMPLETE;
        }
        if(OrderState.CHARGE_PEND.equals(orderState)){
            return OrderStateTranslate.INCOMPLETE;
        }
        return null;
    }
}
