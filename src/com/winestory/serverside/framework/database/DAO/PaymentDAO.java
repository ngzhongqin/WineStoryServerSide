package com.winestory.serverside.framework.database.DAO;

import com.winestory.serverside.framework.database.Entity.PaymentEntity;
import com.winestory.serverside.framework.database.PersistenceManager;
import org.apache.log4j.Logger;


/**
 * Created by zhongqinng on 30/8/15.
 * PaymentDAO
 */
public class PaymentDAO {
    public Logger logger = Logger.getLogger(PaymentDAO.class);
    private PersistenceManager persistenceManager;

    public PaymentDAO(PersistenceManager persistenceManager){
        this.persistenceManager = persistenceManager;
    }


    public PaymentEntity getPaymentEntity(long payment_id){
        PaymentEntity PaymentEntity = null;
        try {
            PaymentEntity = (PaymentEntity)
                    persistenceManager.getEm()
                            .createQuery("SELECT payment FROM PaymentEntity payment where payment.id = :payment_id ")
                            .setParameter("payment_id", payment_id)
                            .getSingleResult();
        }catch (Exception e){
            logger.error("getPaymentEntity: payment_id:"+payment_id+" ERROR: "+e.getMessage());
        }

        return PaymentEntity;
    }

    public PaymentEntity setPaymentStateAndErrorNoCommit(long payment_id,
                                                         String paymentState,
                                                         String paymentError,
                                                         String stripeTxnId,
                                                         String stripeTxnDescription,
                                                         String stripeResponse){
        PaymentEntity paymentEntity = getPaymentEntity(payment_id);
        paymentEntity.setPayment_state(paymentState);
        paymentEntity.setPayment_error(paymentError);
        paymentEntity.setStripe_txn_id(stripeTxnId);
        paymentEntity.setStripe_txn_description(stripeTxnDescription);
        paymentEntity.setStripe_response(stripeResponse);


        return paymentEntity;
    }

}
