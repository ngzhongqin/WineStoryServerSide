package com.winestory.serverside.handler.cart;

import com.winestory.serverside.framework.JSONHelper;
import com.winestory.serverside.framework.VO.OrderItemVO;
import com.winestory.serverside.framework.VO.OrderVO;
import com.winestory.serverside.framework.VO.UserVO;
import com.winestory.serverside.framework.VO.WineVO;
import com.winestory.serverside.framework.database.DAO.WineDAO;
import com.winestory.serverside.framework.database.PersistenceManager;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by zhongqinng on 30/8/15.
 * CartJSONHelper
 */
public class CartJSONHelper {
    public Logger logger = Logger.getLogger(CartJSONHelper.class);
    private JSONHelper jsonHelper;
    public CartJSONHelper(){
        jsonHelper = new JSONHelper();
    }

    public OrderVO getOrderVO(JSONObject data,UserVO userVO, PersistenceManager persistenceManager){
        OrderVO orderVO = new OrderVO();
        JSONObject checkout = jsonHelper.getJSONObject(data, "checkout");
        String name = jsonHelper.getString(checkout, "name");
        String email = jsonHelper.getString(checkout, "email");
        String address = jsonHelper.getString(checkout,"address");
        String postal_code = jsonHelper.getString(checkout,"postal_code");
        String mobile = jsonHelper.getString(checkout,"mobile");
        String other_instructions = jsonHelper.getString(checkout,"other_instructions");

        logger.info("OrderVO name: "+name+" email:"+email+" address:"+address
                +" postal_code:"+postal_code+" mobile:"+mobile+" other_instructions:"+other_instructions);

        orderVO.setFull_name(name);
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
        JSONObject cart = jsonHelper.getJSONObject(ngCart,"$cart");
        JSONArray items = jsonHelper.getJSONArray(cart, "items");
        ArrayList<OrderItemVO> orderItemVOArrayList = getOrderItemVO(items, persistenceManager);
        orderVO.setOrderItemVOArrayList(orderItemVOArrayList);


        return orderVO;
    }

    private ArrayList<OrderItemVO> getOrderItemVO(JSONArray items, PersistenceManager persistenceManager){
        ArrayList<OrderItemVO> orderItemVOArrayList = new ArrayList<OrderItemVO>();
        WineDAO wineDAO = new WineDAO(persistenceManager);
        int size = items.length();
        int i = 0;
        while(i<size){
            try {
                int wine_id = jsonHelper.getInt(items.getJSONObject(i),"_id");
                int quantity = jsonHelper.getInt(items.getJSONObject(i),"_quantity");
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
}
