package com.winestory.serverside.framework.payment;

import com.stripe.Stripe;
import com.stripe.exception.*;
import com.stripe.model.Charge;
import com.winestory.serverside.framework.VO.OrderVO;
import com.winestory.serverside.framework.constants.OrderState;
import com.winestory.serverside.framework.constants.PaymentState;
import com.winestory.serverside.framework.database.DAO.OrderDAO;
import com.winestory.serverside.framework.database.PersistenceManager;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zhongqinng on 2/1/16.
 * StripePayment
 */
public class StripePayment {
    public Logger logger = Logger.getLogger(StripePayment.class);
    private PersistenceManager persistenceManager;

    public StripePayment(PersistenceManager persistenceManager){
        this.persistenceManager=persistenceManager;
    }

    public OrderVO charge(OrderVO orderVO){
        logger.info("Method: charge");
        String stripe_txn_id = null;
        String stripe_response = null;
        String stripe_txn_desription = null;
        OrderDAO orderDAO = new OrderDAO(persistenceManager);
        Stripe.apiKey = PaymentConstant.secretKey;
        if(orderVO!=null){

            if(orderVO.getPaymentVO()!=null){
                String token = orderVO.getPaymentVO().getToken();
                // Create the charge on Stripe's servers - this will charge the user's card
                try {
                    Map<String, Object> chargeParams = new HashMap<String, Object>();
                    chargeParams.put("amount", orderVO.getTotal_cost_in_cents()); // amount in cents, again
                    chargeParams.put("currency", "sgd");
                    chargeParams.put("source", token);
                    chargeParams.put("description", "WineStory.sg Order:"+orderVO.getId());

                    try {
                        Charge charge = Charge.create(chargeParams);
                        stripe_txn_id = charge.getId();
                        stripe_response = charge.toString();
                        stripe_txn_desription = charge.getDescription();

                        logger.info("stripe_txn_id:"+stripe_txn_id+" "
                                    +"stripe_txn_desription:"+stripe_txn_desription+" "
                                    +"stripe_response:"+stripe_response
                        );

                        orderVO = orderDAO.saveOrderState(orderVO,
                                OrderState.CHARGE_SUCC,
                                PaymentState.CHARGE_SUCC,
                                null,
                                stripe_txn_id,
                                stripe_txn_desription,
                                stripe_response);

                    } catch (AuthenticationException e) {

                        orderVO = orderDAO.saveOrderState(
                                orderVO,
                                OrderState.CHARGE_FAIL,
                                PaymentState.STRIPE_AUTHENTICATION_EXCEPTION,
                                getErrorMessage(e.getMessage()),
                                stripe_txn_id,
                                stripe_txn_desription,
                                stripe_response
                        );

                    } catch (InvalidRequestException e) {

                        orderVO = orderDAO.saveOrderState(orderVO,
                                OrderState.CHARGE_FAIL,
                                PaymentState.STRIPE_INVALID_REQUEST_EXCEPTION,
                                getErrorMessage(e.getMessage()),
                                stripe_txn_id,
                                stripe_txn_desription,
                                stripe_response
                        );

                    } catch (APIConnectionException e) {

                        orderVO = orderDAO.saveOrderState(orderVO,
                                OrderState.CHARGE_FAIL,
                                PaymentState.STRIPE_API_CONNECTION_EXCEPTION,
                                getErrorMessage(e.getMessage()),
                                stripe_txn_id,
                                stripe_txn_desription,
                                stripe_response);

                    } catch (APIException e) {

                        orderVO = orderDAO.saveOrderState(orderVO,
                                OrderState.CHARGE_FAIL,
                                PaymentState.STRIPE_API_EXCEPTION,
                                getErrorMessage(e.getMessage()),
                                stripe_txn_id,
                                stripe_txn_desription,
                                stripe_response);
                    }
                } catch (CardException e) {

                    orderVO = orderDAO.saveOrderState(orderVO,
                            OrderState.CHARGE_FAIL,
                            PaymentState.STRIPE_CARD_EXCEPTION,
                            getErrorMessage(e.getMessage()),
                            stripe_txn_id,
                            stripe_txn_desription,
                            stripe_response);
                }
            }else{
                logger.error("Method: charge - orderVO.getPaymentVO() is NULL");
            }
        }else{
            logger.error("Method: charge - orderVO is NULL");
        }


        return orderVO;
    }

    private String getErrorMessage(String error){
        if(error!=null){
            if(error.length()<1000){
                return error;
            }else{
                return "ERROR MESSAGE FROM STRIPE TOO LONG TO STORE";
            }
        }else{
            return "NO ERROR MESSAGE";
        }
    }
}
