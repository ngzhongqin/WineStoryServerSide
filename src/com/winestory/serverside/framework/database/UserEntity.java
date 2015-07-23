package com.winestory.serverside.framework.database;

import javax.persistence.*;

/**
 * Created by zhongqinng on 23/7/15.
 */
@Entity
@Table(name = "user", schema = "winestory", catalog = "winestory")
public class UserEntity {
    private String email;

    @Id
    @SequenceGenerator(name="user_id_seq",
            sequenceName="user_id_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="user_id_seq")
    @Column(name = "id", updatable=false)
    private Long id;


    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEntity that = (UserEntity) o;

        if (id != that.id) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;

        return true;
    }

}
