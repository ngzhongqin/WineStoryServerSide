package com.winestory.serverside.framework.database.DAO;

import com.winestory.serverside.framework.VO.SessionVO;
import com.winestory.serverside.framework.database.Entity.SessionEntity;
import com.winestory.serverside.framework.database.PersistManager;
import org.apache.log4j.Logger;

import javax.persistence.EntityTransaction;
import java.sql.Timestamp;

/**
 * Created by zhongqinng on 27/7/15.
 * SessionDAO
 */
public class SessionDAO {
    public Logger logger = Logger.getLogger(SessionDAO.class);
    private PersistManager persistManager;

    public SessionDAO(){
        persistManager = new PersistManager();
    }

    public void createSession(SessionVO sessionVO){
        EntityTransaction tx = persistManager.getEm().getTransaction();
        SessionEntity sessionEntity = new SessionEntity();
        sessionEntity.setId(sessionVO.getId());
        sessionEntity.setUser_id(sessionVO.getUser_id());
        sessionEntity.setTime_created(new Timestamp(System.currentTimeMillis()));
        sessionEntity.setTime_updated(new Timestamp(System.currentTimeMillis()));
        tx.begin();
        persistManager.getEm().persist(sessionEntity);
        tx.commit();
    }
}
