package com.winestory.serverside.framework.database.DAO;

import com.winestory.serverside.framework.VO.WineVO;
import com.winestory.serverside.framework.database.Entity.WineEntity;
import com.winestory.serverside.framework.database.PersistenceManager;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created by zhongqinng on 28/7/15.
 * WineDAO
 */
public class WineDAO {
    public Logger logger = Logger.getLogger(WineDAO.class);
    private PersistenceManager persistenceManager;

    public WineDAO(PersistenceManager persistenceManager){
        this.persistenceManager = persistenceManager;
    };


    public List<WineEntity> getAllWines(){
        List<WineEntity> wineEntityList = null;
        try {
            wineEntityList =
                    persistenceManager.getEm()
                            .createQuery("SELECT w FROM WineEntity w")
                            .getResultList();
        }catch (Exception e){
            logger.error("getAllWines: ERROR: "+e.getMessage());
        }

        return wineEntityList;
    }

    public WineEntity getWine(int wine_id) {
        WineEntity wineEntity = null;
        try {
            wineEntity = (WineEntity)
                    persistenceManager.getEm()
                            .createQuery("SELECT w FROM WineEntity w where w.id = :wine_id ")
                            .setParameter("wine_id", wine_id)
                            .getSingleResult();
        }catch (Exception e){
            logger.error("getWine: wine_id:"+wine_id+" ERROR: "+e.getMessage());
        }

        return wineEntity;
    }

    public WineVO getWineVO(int wine_id) {
        WineEntity wineEntity = getWine(wine_id);
        WineVO wineVO = null;
        if(wineEntity!=null){
            wineVO = new WineVO(wineEntity.getId(),wineEntity.getName(),
                    wineEntity.getImage_path(), wineEntity.getTasting_note(),
                    wineEntity.getYear(),wineEntity.getColour(),
                    wineEntity.getNose(), wineEntity.getPalate(),
                    wineEntity.getGrapes(), wineEntity.getVolume(),
                    wineEntity.getAvailable_stock(), wineEntity.getPrice());
        }
        return wineVO;
    }

}
