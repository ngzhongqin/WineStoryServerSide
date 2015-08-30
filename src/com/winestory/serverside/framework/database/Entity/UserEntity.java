package com.winestory.serverside.framework.database.Entity;

import javax.persistence.*;

/**
 * Created by zhongqinng on 23/7/15.
 * UserEntity
 */
@Entity
@Table(name = "user", schema = "winestory", catalog = "winestory")
public class UserEntity {
    private String email;
    private String full_name;
    private String password_salt_hash;
    private String mobile;
    private String address;
    private String postal_code;

    @Id
    @SequenceGenerator(name="user_id_seq",
            sequenceName="user_id_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="user_id_seq")
    @Column(name = "id", updatable=false)
    private Long id;

    public Long getId(){return this.id;}
    public void setId(Long id) {
        this.id = id;
    }

    @Basic
    @Column(name = "full_name")
    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    @Basic
    @Column(name = "email")
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Basic
    @Column(name = "password_salt_hash")
    public String getPassword_salt_hash() {
        return password_salt_hash;
    }

    public void setPassword_salt_hash(String password_salt_hash) {
        this.password_salt_hash = password_salt_hash;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserEntity that = (UserEntity) o;

        if (!id .equals( that.id)) return false;
        if (email != null ? !email.equals(that.email) : that.email != null) return false;

        return true;
    }

    @Basic
    @Column(name = "mobile")
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Basic
    @Column(name = "address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Basic
    @Column(name = "postal_code")
    public String getPostal_code() {
        return postal_code;
    }

    public void setPostal_code(String postal_code) {
        this.postal_code = postal_code;
    }
}
