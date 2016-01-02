package com.winestory.serverside.framework.database.DAO;

import com.winestory.serverside.framework.VO.CartVO;
import com.winestory.serverside.framework.constants.OrderState;
import com.winestory.serverside.framework.database.Entity.CartEntity;
import com.winestory.serverside.framework.database.Entity.CartItemEntity;
import com.winestory.serverside.framework.database.PersistenceManager;
import org.apache.log4j.Logger;

import javax.persistence.EntityTransaction;
import java.sql.Timestamp;


/**
 * Created by zhongqinng on 30/8/15.
 * CartDAO
 */
public class CartDAO {
    public Logger logger = Logger.getLogger(CartDAO.class);
    private PersistenceManager persistenceManager;

    public CartDAO(PersistenceManager persistenceManager){
        this.persistenceManager = persistenceManager;
    }

    public void createCart(CartVO cartVO){
        EntityTransaction tx = persistenceManager.getEm().getTransaction();
        CartEntity cartEntity = new CartEntity();
        cartEntity.setMobile(cartVO.getMobile());
        cartEntity.setAddress(cartVO.getAddress());
        cartEntity.setPostal_code(cartVO.getPostal_code());
        cartEntity.setFull_name(cartVO.getFull_name());
        cartEntity.setEmail(cartVO.getEmail());
        cartEntity.setCreateddt(new Timestamp(System.currentTimeMillis()));
        cartEntity.setUser_id(cartVO.getUser_id());
        cartEntity.setTotal_cost(cartVO.getTotal_cost());
        cartEntity.setSub_total(cartVO.getSub_total());
        cartEntity.setShipping_cost(cartVO.getShipping_cost());
        cartEntity.setOther_instructions(cartVO.getOther_instructions());
        cartEntity.setTax(cartVO.getTax());

        logger.info("before tx.begin cartEntity.id:" + cartEntity.getId());
        tx.begin();
        persistenceManager.getEm().persist(cartEntity);
        logger.info("before commit cartEntity.id:" + cartEntity.getId());
        saveCartItems(cartVO, cartEntity.getId());

        tx.commit();
        logger.info("after commit cartEntity.id:"+cartEntity.getId());
    }

    public void saveCartItems(CartVO cartVO, long cartId){
        logger.info("saveOrderItems: cartId:"+cartId);
        if(cartVO!=null) {
            if (cartVO.getCartItemVOArrayList() != null) {
                int size = cartVO.getCartItemVOArrayList().size();
                int i = 0;
                while (i < size) {
                    CartItemEntity cartItemEntity = new CartItemEntity();
                    cartItemEntity.setUser_id(cartVO.getUser_id());
                    cartItemEntity.setQuantity(cartVO.getCartItemVOArrayList().get(i).getQuantity());
                    cartItemEntity.setWine_id(cartVO.getCartItemVOArrayList().get(i).getWine_id());
                    cartItemEntity.setName(cartVO.getCartItemVOArrayList().get(i).getName());
                    cartItemEntity.setCart_id(cartId);
                    cartItemEntity.setUnit_price(cartVO.getCartItemVOArrayList().get(i).getUnit_price_double());
                    cartItemEntity.setCreateddt(new Timestamp(System.currentTimeMillis()));
                    persistenceManager.getEm().persist(cartItemEntity);
                    i++;
                }
            }
        }
    }


}
