package com.winestory.serverside.framework.database.DAO;

import com.winestory.serverside.framework.VO.UserVO;
import com.winestory.serverside.framework.database.Entity.UserEntity;
import com.winestory.serverside.framework.database.PersistManager;
import org.apache.log4j.Logger;

import javax.persistence.EntityTransaction;

/**
 * Created by zhongqinng on 26/7/15.
 */
public class UserDAO {
    public Logger logger = Logger.getLogger(UserDAO.class);
    private PersistManager persistManager;

    public UserDAO(){
        persistManager = new PersistManager();
    };

    public void createNewUser(UserVO userVO){
        EntityTransaction tx = persistManager.getEm().getTransaction();
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(userVO.getEmail());
        userEntity.setFull_name(userVO.getEmail());
        tx.begin();
        persistManager.getEm().persist(userEntity);
        tx.commit();

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
