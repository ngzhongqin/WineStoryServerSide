package com.winestory.serverside.framework.database.DAO;

import com.winestory.serverside.framework.VO.UserVO;
import com.winestory.serverside.framework.database.Entity.UserEntity;
import com.winestory.serverside.framework.database.PersistenceManager;
import org.apache.log4j.Logger;

import javax.persistence.EntityTransaction;
import java.util.List;

/**
 * Created by zhongqinng on 26/7/15.
 * UserDAO
 */
public class UserDAO {
    public Logger logger = Logger.getLogger(UserDAO.class);
    private PersistenceManager persistenceManager;

    public UserDAO(PersistenceManager persistenceManager){

        this.persistenceManager = persistenceManager;
    }

    public UserVO createNewUser(UserVO userVO){
        EntityTransaction tx = persistenceManager.getEm().getTransaction();
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(userVO.getEmail());
        userEntity.setFull_name(userVO.getFull_name());
        userEntity.setPassword_salt_hash(userVO.getPassword_salt_hash());
        tx.begin();
        persistenceManager.getEm().persist(userEntity);
        tx.commit();
        logger.info("createNewUser ID:"+userEntity.getId());
        userVO.setId(userEntity.getId());
        return userVO;
    }

    public boolean checkIfEmailIsTaken(String email){
        boolean returnBoolean = false;

        List<UserEntity> userEntityArrayList =
                persistenceManager.getEm().createQuery("select u from UserEntity u where u.email = :email")
                .setParameter("email", email)
                .getResultList();

        if(userEntityArrayList.size()>0){
            logger.info("checkIfEmailIsTaken: email:"+email+" is taken");
            returnBoolean =true;
        }

        return returnBoolean;
    }

    public UserVO getUser(String email){
        UserVO userVO = null;
        try {
            UserEntity userEntity =
                    (UserEntity) persistenceManager.getEm()
                            .createQuery("SELECT u FROM UserEntity u where u.email= :email")
                            .setParameter("email", email)
                            .getSingleResult();
            if(userEntity!=null){
                userVO = new UserVO(userEntity.getId(),
                                    userEntity.getFull_name(),
                                    userEntity.getEmail(),
                                    userEntity.getPassword_salt_hash());
                userVO.setPostal_code(userEntity.getPostal_code());
                userVO.setAddress(userEntity.getAddress());
                userVO.setMobile(userEntity.getMobile());
            }
        }catch (Exception e){
            logger.error("getUser: ERROR: "+e.getMessage());
        }

        return userVO;
    }

    public UserVO getUserFromSessionId(String sessionId){
        UserVO userVO = null;
        try {
            UserEntity userEntity =
                    (UserEntity) persistenceManager.getEm()
                            .createQuery("SELECT u FROM UserEntity u, SessionEntity s where s.id = :sessionId " +
                                    "and s.user_id = u.id")
                            .setParameter("sessionId", sessionId)
                            .getSingleResult();
            if(userEntity!=null){
                userVO = new UserVO(userEntity.getId(),
                        userEntity.getFull_name(),
                        userEntity.getEmail(),
                        userEntity.getPassword_salt_hash());
                userVO.setPostal_code(userEntity.getPostal_code());
                userVO.setAddress(userEntity.getAddress());
                userVO.setMobile(userEntity.getMobile());
            }
        }catch (Exception e){
            logger.error("getUser: ERROR: "+e.getMessage());
        }

        return userVO;
    }

}
