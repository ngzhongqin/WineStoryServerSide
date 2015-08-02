package com.winestory.serverside.handler.store;

import com.winestory.serverside.framework.JSONHelper;
import com.winestory.serverside.framework.VO.WineVO;
import com.winestory.serverside.framework.database.Entity.WineEntity;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhongqinng on 28/7/15.
 * StoreJSONHelper
 */
public class StoreJSONHelper {
    public Logger logger = Logger.getLogger(StoreJSONHelper.class);
    private JSONHelper jsonHelper;
    public StoreJSONHelper(){
        jsonHelper = new JSONHelper();
    }

    public WineVO getWineVO(WineEntity wineEntity){
        Long id = wineEntity.getId();
        String name = wineEntity.getName();
        String image_path = wineEntity.getImage_path();
        String tasting_note = wineEntity.getTasting_note();
        String year = wineEntity.getYear();
        String colour =wineEntity.getColour();
        String nose = wineEntity.getNose();
        String palate =wineEntity.getPalate();
        String grapes =wineEntity.getGrapes();
        int volume=wineEntity.getVolume();
        int available_stock=wineEntity.getAvailable_stock();
        Number price=wineEntity.getPrice();

        return new WineVO(id,name,image_path,tasting_note,year,colour,nose,palate,grapes,volume,available_stock,price);
    }

    public ArrayList<WineVO> getWineVOList(List<WineEntity> wineEntityList){
        ArrayList<WineVO> wineVOArrayList = new ArrayList<WineVO>();
        int size = wineEntityList.size();
        int i = 0;
        while(i<size){
            WineVO wineVO = getWineVO(wineEntityList.get(i));
            wineVOArrayList.add(wineVO);
            i++;
        }
        return wineVOArrayList;
    }

    public JSONObject getJSONObject(List<WineEntity> wineEntityList){
        JSONArray jsonArray = new JSONArray();
        int size = wineEntityList.size();
        int i = 0;
        while(i<size){
            JSONObject taskJSON = loadTaskEntityIntoJSON(wineEntityList.get(i));
            jsonArray.put(taskJSON);
            i++;
        }
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("data", jsonArray);

        } catch (JSONException e) {
            logger.error("getJSONObject: error:"+e.getMessage());
            e.printStackTrace();
        }

        return jsonObject;
    }

    private JSONObject loadTaskEntityIntoJSON(WineEntity wineEntity){
        JSONObject wineJSON = new JSONObject();
        try {
            wineJSON.put("id",wineEntity.getId());
            wineJSON.put("name",wineEntity.getName());
            wineJSON.put("image_path",wineEntity.getImage_path());
            wineJSON.put("tasting_note",wineEntity.getTasting_note());
            wineJSON.put("year",wineEntity.getYear());
            wineJSON.put("colour",wineEntity.getColour());
            wineJSON.put("nose",wineEntity.getNose());
            wineJSON.put("palate",wineEntity.getPalate());
            wineJSON.put("grapes",wineEntity.getGrapes());
            wineJSON.put("volume",wineEntity.getVolume());
            wineJSON.put("available_stock",wineEntity.getAvailable_stock());
            wineJSON.put("price",wineEntity.getPrice());


        } catch (JSONException e) {
            e.printStackTrace();
        }
        return wineJSON;
    }


    public JSONObject getJSONObject(WineEntity wineEntity) {
        JSONObject jsonObject = new JSONObject();

        try {
            jsonObject.put("data", loadTaskEntityIntoJSON(wineEntity));

        } catch (JSONException e) {
            logger.error("getJSONObject: error:"+e.getMessage());
            e.printStackTrace();
        }

        return jsonObject;
    }
}
