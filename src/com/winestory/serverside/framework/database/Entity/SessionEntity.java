package com.winestory.serverside.framework.database.Entity;

import javax.persistence.*;
import java.sql.Timestamp;

/**
 * Created by zhongqinng on 23/7/15.
 * SessionEntity
 */
@Entity
@Table(name = "session", schema = "winestory", catalog = "winestory")
public class SessionEntity {

    private Long user_id;
    private Timestamp time_created;
    private Timestamp time_updated;


    @Id
    @Column(name = "id")
    private String id;

    public String getId(){
        return this.id;
    }


    public void setId(String id) {
        this.id = id;
    }

    @Basic
    @Column(name = "user_id")
    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    @Basic
    @Column(name = "time_created")
    public Timestamp getTime_created() {
        return time_created;
    }

    public void setTime_created(Timestamp time_created) {
        this.time_created = time_created;
    }

    @Basic
    @Column(name = "time_updated")
    public Timestamp getTime_updated() {
        return time_updated;
    }

    public void setTime_updated(Timestamp time_updated) {
        this.time_updated = time_updated;
    }


}
