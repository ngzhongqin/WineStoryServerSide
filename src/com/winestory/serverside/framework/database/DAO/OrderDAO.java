package com.winestory.serverside.framework.database.DAO;

import com.winestory.serverside.framework.VO.OrderItemVO;
import com.winestory.serverside.framework.VO.OrderVO;
import com.winestory.serverside.framework.VO.PaymentVO;
import com.winestory.serverside.framework.VO.UserVO;
import com.winestory.serverside.framework.constants.OrderState;
import com.winestory.serverside.framework.constants.PaymentState;
import com.winestory.serverside.framework.database.Entity.OrderEntity;
import com.winestory.serverside.framework.database.Entity.OrderItemEntity;
import com.winestory.serverside.framework.database.Entity.PaymentEntity;
import com.winestory.serverside.framework.database.PersistenceManager;
import com.winestory.serverside.handler.order.OrderConstants;
import org.apache.log4j.Logger;

import javax.persistence.EntityTransaction;
import java.sql.Timestamp;
import java.util.*;


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

    public OrderVO createOrder(OrderVO orderVO){
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
        orderEntity.setOrder_state(OrderState.CHARGE_PEND);


        logger.info("before tx.begin orderEntity.id:" + orderEntity.getId());
        tx.begin();

        persistenceManager.getEm().persist(orderEntity);
        logger.info("before commit orderEntity.id:" + orderEntity.getId());
        saveOrderItems(orderVO, orderEntity.getId());
        long payment_id = savePaymentDetails(orderVO,orderEntity.getId());
        orderEntity.setPayment_id(payment_id);
        persistenceManager.getEm().persist(orderEntity);
        tx.commit();
        logger.info("after commit orderEntity.id:"+orderEntity.getId());

        orderVO.setId(orderEntity.getId());
        return orderVO;
    }

    public long savePaymentDetails(OrderVO orderVO, long orderId){
        logger.info("savePaymentDetails: orderId:"+orderId);

        PaymentEntity paymentEntity = new PaymentEntity();
        paymentEntity.setPayment_state(PaymentState.CHARGE_PEND);
        if(orderVO!=null){
            if(orderVO.getPaymentVO()!=null){
                paymentEntity.setToken(orderVO.getPaymentVO().getToken());
            }else{
                logger.error("createOrder: orderVO.getPaymentVO() is NULL");
            }
            paymentEntity.setUser_id(orderVO.getUser_id());
        }

        paymentEntity.setCreateddt(new Timestamp(System.currentTimeMillis()));
        paymentEntity.setOrder_id(orderId);
        paymentEntity.setTotal_cost(orderVO.getTotal_cost());
        persistenceManager.getEm().persist(paymentEntity);
        return  paymentEntity.getId();
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

    public OrderVO saveOrderState(OrderVO orderVO,
                                  String orderState,
                                  String paymentState,
                                  String paymentError,
                                  String stripeTxnId,
                                  String stripeTxnDescription,
                                  String stripeResponse){
        OrderEntity orderEntity = getOrderEntity(orderVO.getId());
        orderEntity.setOrder_state(orderState);

        PaymentDAO paymentDAO = new PaymentDAO(persistenceManager);
        PaymentEntity paymentEntity = paymentDAO.setPaymentStateAndErrorNoCommit(
                orderEntity.getPayment_id(),
                paymentState,
                paymentError,
                stripeTxnId,
                stripeTxnDescription,
                stripeResponse);

        EntityTransaction tx = persistenceManager.getEm().getTransaction();
        tx.begin();

        persistenceManager.getEm().persist(orderEntity);
        persistenceManager.getEm().persist(paymentEntity);

        tx.commit();

        orderVO.setOrder_state(orderState);

        PaymentVO paymentVO = orderVO.getPaymentVO();
        paymentVO.setPayment_error(paymentError);
        paymentVO.setPayment_state(paymentState);
        paymentVO.setStripeTxnId(stripeTxnId);
        paymentVO.setStripeTxnDescription(stripeTxnDescription);
        orderVO.setPaymentVO(paymentVO);

        return orderVO;
    }

    public OrderEntity getOrderEntity(long order_id){
        OrderEntity orderEntity = null;
        try {
            orderEntity = (OrderEntity)
                    persistenceManager.getEm()
                            .createQuery("SELECT order FROM OrderEntity order where order.id = :order_id ")
                            .setParameter("order_id", order_id)
                            .getSingleResult();
        }catch (Exception e){
            logger.error("getOrderEntity: order_id:"+order_id+" ERROR: "+e.getMessage());
        }

        return orderEntity;
    }


    public List<OrderEntity> getUserOrdersEntityList(UserVO userVO) {
        List<OrderEntity> orderEntityList = null;
        if(userVO!=null){
            try {
                Set order_state = new HashSet();
                order_state.add(OrderState.CHARGE_SUCC);
                order_state.add(OrderState.DELIVERY_PEND);
                order_state.add(OrderState.DELIVERY_SUCC);

                orderEntityList =
                        persistenceManager.getEm()
                                .createQuery("SELECT o FROM OrderEntity o where o.user_id = :user_id " +
                                        "AND o.order_state IN :order_state")
                                .setParameter("user_id", userVO.getId())
                                .setParameter("order_state", order_state)
                                .getResultList();
            }catch (Exception e){
                logger.error("getUserOrdersEntityList: ERROR: "+e.getMessage());
            }

        }else{
            logger.error("getUserOrdersEntityList: userVO is NULL");
        }
        return orderEntityList;
    }

    public ArrayList<OrderVO> getUserOrderVOArrayList(UserVO userVO) {
        ArrayList<OrderVO> orderVOArrayList = null;
        List<OrderEntity> orderEntityList = getUserOrdersEntityList(userVO);
        if(orderEntityList!=null){
            orderVOArrayList = loadIntoVOList(orderEntityList);
        }
        return orderVOArrayList;
    }

    private ArrayList<OrderVO> loadIntoVOList(List<OrderEntity> orderEntityList) {
        ArrayList<OrderVO> orderVOArrayList = new ArrayList<OrderVO>();
        int size = orderEntityList.size();
        logger.info("loadIntoVOList: size="+size);
        int i = 0;
        while(i<size){
            OrderVO orderVO = new OrderVO();
            orderVO.setId(orderEntityList.get(i).getId());
            orderVO.setOrder_state(orderEntityList.get(i).getOrder_state());
            orderVO.setCreateddt(orderEntityList.get(i).getCreateddt());
            orderVO.setTotal_cost(orderEntityList.get(i).getTotal_cost());

            orderVOArrayList.add(orderVO);
            i++;
        }
        return orderVOArrayList;
    }

    public OrderVO getOrderVO(int order_id, UserVO userVO) {
        OrderVO orderVO = null;
        OrderEntity orderEntity = getOrderEntity(order_id);
        if(orderEntity!=null){
            if(orderEntity.getUser_id()==userVO.getId()){
                orderVO=loadOrderEntityIntoOrderVO(orderEntity);
                orderVO=loadOrderItemsIntoOrderVO(orderVO,order_id);
            }
        }
        return orderVO;
    }

    private OrderVO loadOrderItemsIntoOrderVO(OrderVO orderVO, int order_id) {
        List<OrderItemEntity> orderItemEntityList = getOrderItemEntityList(order_id);
        if(orderItemEntityList!=null){
            ArrayList<OrderItemVO> orderItemVOArrayList = new ArrayList<>();
            int i = 0;
            int size = orderItemEntityList.size();
            while(i<size){
                OrderItemVO orderItemVO = new OrderItemVO();
                orderItemVO.setId(orderItemEntityList.get(i).getId());
                orderItemVO.setQuantity(orderItemEntityList.get(i).getQuantity());
                orderItemVO.setWine_id(orderItemEntityList.get(i).getWine_id());
                orderItemVO.setUnit_price(orderItemEntityList.get(i).getUnit_price());
                orderItemVO.setName(orderItemEntityList.get(i).getName());
                orderItemVO.setCreateddt(orderItemEntityList.get(i).getCreateddt());
                orderItemVOArrayList.add(orderItemVO);
                i++;
            }
            orderVO.setOrderItemVOArrayList(orderItemVOArrayList);
        }
        return orderVO;
    }

    private OrderVO loadOrderEntityIntoOrderVO(OrderEntity orderEntity){
        OrderVO orderVO= null;
        if(orderEntity!=null){
            orderVO = new OrderVO();
            orderVO.setId(orderEntity.getId());
            orderVO.setTotal_cost(orderEntity.getTotal_cost());
            orderVO.setCreateddt(orderEntity.getCreateddt());
            orderVO.setOrder_state(orderEntity.getOrder_state());
            orderVO.setEmail(orderEntity.getEmail());
            orderVO.setAddress(orderEntity.getAddress());
            orderVO.setFull_name(orderEntity.getFull_name());
            orderVO.setMobile(orderEntity.getMobile());
            orderVO.setOther_instructions(orderEntity.getOther_instructions());
            orderVO.setPostal_code(orderEntity.getPostal_code());
            orderVO.setTax(orderEntity.getTax());
            orderVO.setShipping_cost(orderEntity.getShipping_cost());
            orderVO.setSub_total(orderEntity.getSub_total());
        }
        return orderVO;
    }

    public List<OrderItemEntity> getOrderItemEntityList(int order_id) {
        List<OrderItemEntity> orderItemEntityList = null;
            try {
                orderItemEntityList =
                        persistenceManager.getEm()
                                .createQuery("SELECT o FROM OrderItemEntity o where o.order_id = :order_id ")
                                .setParameter("order_id", order_id)
                                .getResultList();
            }catch (Exception e){
                logger.error("getOrderItemEntityList: ERROR: "+e.getMessage());
            }


        return orderItemEntityList;
    }
}
