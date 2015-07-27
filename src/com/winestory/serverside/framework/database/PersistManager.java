package com.winestory.serverside.framework.database;

import org.apache.log4j.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * Created by zhongqinng on 23/7/15.
 * PersistManager
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

    public void close(){
        em.close();
    }

}
