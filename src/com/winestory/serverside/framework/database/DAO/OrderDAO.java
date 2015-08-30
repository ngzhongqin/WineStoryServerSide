package com.winestory.serverside.framework.database.DAO;

import com.winestory.serverside.framework.VO.OrderVO;
import com.winestory.serverside.framework.database.Entity.OrderEntity;
import com.winestory.serverside.framework.database.Entity.OrderItemEntity;
import com.winestory.serverside.framework.database.PersistenceManager;
import org.apache.log4j.Logger;

import javax.persistence.EntityTransaction;
import java.sql.Timestamp;


/**
 * Created by zhongqinng on 30/8/15.
 * OrderDAO
 */
public class OrderDAO {
    public Logger logger = Logger.getLogger(OrderDAO.class);
    private PersistenceManager persistenceManager;

    public OrderDAO(PersistenceManager persistenceManager){
        this.persistenceManager = persistenceManager;
    }

    public void createOrder(OrderVO orderVO){
        EntityTransaction tx = persistenceManager.getEm().getTransaction();
        OrderEntity orderEntity = new OrderEntity();
        orderEntity.setMobile(orderVO.getMobile());
        orderEntity.setAddress(orderVO.getAddress());
        orderEntity.setPostal_code(orderVO.getPostal_code());
        orderEntity.setFull_name(orderVO.getFull_name());
        orderEntity.setEmail(orderVO.getEmail());
        orderEntity.setCreateddt(new Timestamp(System.currentTimeMillis()));
        orderEntity.setUser_id(orderVO.getUser_id());
        orderEntity.setTotal_cost(orderVO.getTotal_cost());
        orderEntity.setSub_total(orderVO.getSub_total());
        orderEntity.setShipping_cost(orderVO.getShipping_cost());
        orderEntity.setOther_instructions(orderVO.getOther_instructions());
        orderEntity.setTax(orderVO.getTax());

        logger.info("before tx.begin orderEntity.id:" + orderEntity.getId());
        tx.begin();
        persistenceManager.getEm().persist(orderEntity);
        logger.info("before commit orderEntity.id:" + orderEntity.getId());
        saveOrderItems(orderVO, orderEntity.getId());

        tx.commit();
        logger.info("after commit orderEntity.id:"+orderEntity.getId());
    }

    public void saveOrderItems(OrderVO orderVO, long orderId){
        logger.info("saveOrderItems: orderId:"+orderId);
        if(orderVO!=null) {
            if (orderVO.getOrderItemVOArrayList() != null) {
                int size = orderVO.getOrderItemVOArrayList().size();
                int i = 0;
                while (i < size) {
                    OrderItemEntity orderItemEntity = new OrderItemEntity();
                    orderItemEntity.setUser_id(orderVO.getUser_id());
                    orderItemEntity.setQuantity(orderVO.getOrderItemVOArrayList().get(i).getQuantity());
                    orderItemEntity.setWine_id(orderVO.getOrderItemVOArrayList().get(i).getWine_id());
                    orderItemEntity.setName(orderVO.getOrderItemVOArrayList().get(i).getName());
                    orderItemEntity.setOrder_id(orderId);
                    orderItemEntity.setUnit_price(orderVO.getOrderItemVOArrayList().get(i).getUnit_price_double());
                    orderItemEntity.setCreateddt(new Timestamp(System.currentTimeMillis()));
                    persistenceManager.getEm().persist(orderItemEntity);
                    i++;
                }
            }
        }
    }


}
