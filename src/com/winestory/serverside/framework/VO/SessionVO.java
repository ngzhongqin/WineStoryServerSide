package com.winestory.serverside.framework.VO;

import java.sql.Timestamp;

/**
 * Created by zhongqinng on 27/7/15.
 * SessionVO
 */
public class SessionVO {
    private String id;
    private Long user_id;
    private Timestamp time_created;
    private Timestamp time_updated;

    public SessionVO(String id,
                     Long user_id){
        this.id=id;
        this.user_id=user_id;
    }

    public SessionVO(String id,
                     Long user_id,
                     Timestamp time_created,
                     Timestamp time_updated){
        this.id=id;
        this.user_id=user_id;
        this.time_created=time_created;
        this.time_updated=time_updated;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Timestamp getTime_created() {
        return time_created;
    }

    public void setTime_created(Timestamp time_created) {
        this.time_created = time_created;
    }

    public Timestamp getTime_updated() {
        return time_updated;
    }

    public void setTime_updated(Timestamp time_updated) {
        this.time_updated = time_updated;
    }
}
