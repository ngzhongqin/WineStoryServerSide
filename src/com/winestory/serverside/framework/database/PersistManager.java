package com.winestory.serverside.framework.database;

import com.winestory.serverside.framework.database.Entity.UserEntity;
import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

/**
 * Created by zhongqinng on 23/7/15.
 */
public class PersistManager {
    public Logger logger = Logger.getLogger(PersistManager.class);
    private EntityManagerFactory entityManagerFactory;
    private EntityManager em;

    public PersistManager(){
        entityManagerFactory = Persistence.createEntityManagerFactory("winestory");
        em = entityManagerFactory.createEntityManager();
    }

    public EntityManager getEm(){
        return em;
    }

    /*
    public void save(String email){
        logger.info("Method: save");
        EntityTransaction tx = em.getTransaction();
        UserEntity user = new UserEntity();
        user.setEmail(email);
        tx.begin();
        em.persist(user);
        tx.commit();
        logger.info("Method: save end");
    }
    */
}
