package com.winestory.serverside.framework.database.DAO;

import com.winestory.serverside.framework.VO.SessionVO;
import com.winestory.serverside.framework.database.Entity.SessionEntity;
import com.winestory.serverside.framework.database.Entity.WineEntity;
import com.winestory.serverside.framework.database.PersistenceManager;
import org.apache.log4j.Logger;

import javax.persistence.EntityTransaction;
import java.sql.Timestamp;

/**
 * Created by zhongqinng on 27/7/15.
 * SessionDAO
 */
public class SessionDAO {
    public Logger logger = Logger.getLogger(SessionDAO.class);
    private PersistenceManager persistenceManager;

    public SessionDAO(PersistenceManager persistenceManager){
        this.persistenceManager = persistenceManager;
    }

    public void createSession(SessionVO sessionVO){
        EntityTransaction tx = persistenceManager.getEm().getTransaction();
        SessionEntity sessionEntity = new SessionEntity();
        sessionEntity.setId(sessionVO.getId());
        sessionEntity.setUser_id(sessionVO.getUser_id());
        sessionEntity.setTime_created(new Timestamp(System.currentTimeMillis()));
        sessionEntity.setTime_updated(new Timestamp(System.currentTimeMillis()));
        tx.begin();
        persistenceManager.getEm().persist(sessionEntity);
        tx.commit();
    }

    public long getUserId(String session_id){
        long returnUserId = -1;
        try {
            SessionEntity sessionEntity = (SessionEntity)
                    persistenceManager.getEm()
                            .createQuery("SELECT s FROM SessionEntity s where s.id = :session_id ")
                            .setParameter("session_id", session_id)
                            .getSingleResult();
            if(session_id!=null){
                returnUserId = sessionEntity.getUser_id();
            }
        }catch (Exception e){
            logger.error("getUserId: session_id:"+session_id+" ERROR: "+e.getMessage());
        }

        return returnUserId;
    }
}
