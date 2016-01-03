package com.winestory.serverside.framework.helper;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

/**
 * Created by zhongqinng on 3/1/16.
 * DateHelper
 */
public class DateHelper {
    public static String returnDate(Timestamp timestamp){
        String date1 = null;
        if(timestamp!=null){
            String PATTERN="yyyy-MMM-dd";
            SimpleDateFormat dateFormat=new SimpleDateFormat();
            dateFormat.applyPattern(PATTERN);
            date1=dateFormat.format(timestamp.getTime());
        }


        return date1;
    }
}
