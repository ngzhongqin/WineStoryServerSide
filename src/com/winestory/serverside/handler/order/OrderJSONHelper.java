package com.winestory.serverside.handler.order;

import com.winestory.serverside.framework.JSONHelper;
import com.winestory.serverside.framework.VO.*;
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
        return null;
    }
}
