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
    private PersistenceManager persistManager;

    public UserDAO(){
        persistManager = new PersistenceManager();
    };

    public void close(){
        persistManager.close();
    }

    public void createNewUser(UserVO userVO){
        EntityTransaction tx = persistManager.getEm().getTransaction();
        UserEntity userEntity = new UserEntity();
        userEntity.setEmail(userVO.getEmail());
        userEntity.setFull_name(userVO.getFull_name());
        userEntity.setPassword_salt_hash(userVO.getPassword_salt_hash());
        tx.begin();
        persistManager.getEm().persist(userEntity);
        tx.commit();

    }

    public boolean checkIfEmailIsTaken(String email){
        boolean returnBoolean = false;

        List<UserEntity> userEntityArrayList =
                persistManager.getEm().createQuery("select u from UserEntity u where u.email = :email")
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
                    (UserEntity) persistManager.getEm()
                            .createQuery("SELECT u FROM UserEntity u where u.email= :email")
                            .setParameter("email", email)
                            .getSingleResult();
            if(userEntity!=null){
                userVO = new UserVO(userEntity.getId(),
                                    userEntity.getFull_name(),
                                    userEntity.getEmail(),
                                    userEntity.getPassword_salt_hash());
            }
        }catch (Exception e){
            logger.error("getUser: ERROR: "+e.getMessage());
        }

        return userVO;
    }


}
